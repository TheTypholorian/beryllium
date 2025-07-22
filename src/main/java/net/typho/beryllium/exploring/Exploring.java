package net.typho.beryllium.exploring;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.DyeColor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.structure.Structure;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.Module;

public class Exploring extends Module {
    public final LootFunctionType<ExplorationCompassLootFunction> EXPLORATION_COMPASS = Registry.register(Registries.LOOT_FUNCTION_TYPE, id("exploration_compass"), new LootFunctionType<>(ExplorationCompassLootFunction.CODEC));

    public final ComponentType<DyeColor> COMPASS_NEEDLE_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, id("needle_color"), ComponentType.<DyeColor>builder().codec(DyeColor.CODEC).build());

    public final StructureProcessorType<StoneBrickVariantProcessor> STONE_BRICK_VARIANT_PROCESSOR = Registry.register(Registries.STRUCTURE_PROCESSOR, id("stone_brick_variants"), () -> StoneBrickVariantProcessor.CODEC);
    public final StructureProcessorType<SusSandProcessor> SUS_SAND_PROCESSOR = Registry.register(Registries.STRUCTURE_PROCESSOR, id("sus_sand"), () -> SusSandProcessor.CODEC);
    public final StructureProcessorType<ContainerContentsProcessor> CONTAINER_CONTENTS_PROCESSOR = Registry.register(Registries.STRUCTURE_PROCESSOR, id("container_contents"), () -> ContainerContentsProcessor.CODEC);

    public final SimpleParticleType FIREFLY_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, id("firefly"), FabricParticleTypes.simple(false));
    public final SimpleParticleType BIRCH_LEAVES_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, id("birch_leaves"), FabricParticleTypes.simple(false));

    public final TagKey<Structure> SPAWN_KEY = TagKey.of(RegistryKeys.STRUCTURE, id("spawn"));
    public final TagKey<Biome> HAS_FIREFLIES = TagKey.of(RegistryKeys.BIOME, id("has_fireflies"));
    public final TagKey<Biome> BIRCH_TAG = TagKey.of(RegistryKeys.BIOME, id("birch"));

    public final Block FIREFLY_BOTTLE =
            blockWithItem(
                    "firefly_bottle",
                    new FireflyBottleBlock(AbstractBlock.Settings.create()
                            .strength(0f)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .emissiveLighting((state, world, pos) -> true)
                            .luminance(state -> 3)
                            .breakInstantly()
                            .noBlockBreakParticles()
                            .nonOpaque()
                            .sounds(BlockSoundGroup.GLASS)
                            .suffocates(Blocks::never)
                            .blockVision(Blocks::never)),
            new Item.Settings()
    );
    public final Block DAFFODILS = blockWithItem("daffodils", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public final Block ALGAE_BLOCK = block("algae", new AlgaeBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_GREEN)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .sounds(BlockSoundGroup.GLOW_LICHEN)
            .nonOpaque()
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)));

    public final Item METAL_DETECTOR_ITEM = item("metal_detector", new MetalDetectorItem(new Item.Settings()));
    public final Item ALGAE_ITEM = item("algae", new AlgaeItem(new Item.Settings(), ALGAE_BLOCK));
    public final Item EXODINE_INGOT = item("exodine_ingot", new Item(new Item.Settings()));

    public final RegistryKey<ConfiguredFeature<?, ?>> ALGAE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id("algae"));
    public final RegistryKey<ConfiguredFeature<?, ?>> DAFFODILS_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id("daffodils"));

    public final RegistryKey<PlacedFeature> ALGAE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("algae"));
    public final RegistryKey<PlacedFeature> DAFFODILS_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("daffodils"));

    public Exploring(String name) {
        super(name);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    entries.addAfter(Items.COMPASS, METAL_DETECTOR_ITEM);
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
                .register(entries -> {
                    entries.addAfter(Items.PINK_PETALS, DAFFODILS);
                });
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(Items.COMPASS, builder -> builder.add(COMPASS_NEEDLE_COMPONENT, DyeColor.RED)));
        Registry.register(Registries.RECIPE_TYPE, id("compass_dye"), new RecipeType<>() {
            @Override
            public String toString() {
                return "exploring/compass_dye";
            }
        });
        Registry.register(Registries.RECIPE_SERIALIZER, id("compass_dye"), CompassDyeRecipe.SERIALIZER);
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SWAMP),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ALGAE_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BIRCH_TAG),
                GenerationStep.Feature.VEGETAL_DECORATION,
                DAFFODILS_PLACED
        );
        BiomeModifications.create(Beryllium.EXPLORING.id("fireflies"))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(Beryllium.EXPLORING.HAS_FIREFLIES), context -> {
                    context.getEffects().setParticleConfig(new BiomeParticleConfig(Beryllium.EXPLORING.FIREFLY_PARTICLE, 0.008f));
                });
        BiomeModifications.create(Beryllium.EXPLORING.id("swamp_water"))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(BiomeKeys.SWAMP), context -> {
                    context.getEffects().setWaterColor(0x6D6D5C);
                    context.getEffects().setWaterFogColor(0x6D6D5C);
                });
    }

    public static class Config extends ConfigSection {
        public MetalDetector metalDetector = new MetalDetector();

        public static class MetalDetector extends ConfigSection {
            public int tooltipRadius = 16, needleX = 16, needleY = 2;
        }

        public Structures structures = new Structures();

        public static class Structures extends ConfigSection {
            public boolean junglePyramid = true;
            public boolean desertPyramid = true;
        }

        public boolean spawnInVillage = true;
    }
}
