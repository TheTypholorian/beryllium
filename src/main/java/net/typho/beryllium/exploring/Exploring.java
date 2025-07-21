package net.typho.beryllium.exploring;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
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
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.structure.Structure;
import net.typho.beryllium.Module;

public class Exploring extends Module {
    public final Item METAL_DETECTOR_ITEM = item("metal_detector", new MetalDetectorItem(new Item.Settings()));
    public final LootFunctionType<ExplorationCompassLootFunction> EXPLORATION_COMPASS = Registry.register(Registries.LOOT_FUNCTION_TYPE, id("exploration_compass"), new LootFunctionType<>(ExplorationCompassLootFunction.CODEC));
    public final TagKey<Structure> SPAWN_KEY = TagKey.of(RegistryKeys.STRUCTURE, id("spawn"));
    public final ComponentType<DyeColor> COMPASS_NEEDLE_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, id("needle_color"), ComponentType.<DyeColor>builder().codec(DyeColor.CODEC).build());
    public final StructureProcessorType<StoneBrickVariantProcessor> STONE_BRICK_VARIANT_PROCESSOR = Registry.register(Registries.STRUCTURE_PROCESSOR, id("stone_brick_variants"), () -> StoneBrickVariantProcessor.CODEC);
    public final StructureProcessorType<SusSandProcessor> SUS_SAND_PROCESSOR = Registry.register(Registries.STRUCTURE_PROCESSOR, id("sus_sand"), () -> SusSandProcessor.CODEC);
    public final StructureProcessorType<ContainerContentsProcessor> CONTAINER_CONTENTS_PROCESSOR = Registry.register(Registries.STRUCTURE_PROCESSOR, id("container_contents"), () -> ContainerContentsProcessor.CODEC);
    public final SimpleParticleType FIREFLY_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, id("firefly"), FabricParticleTypes.simple(false));
    public final TagKey<Biome> HAS_FIREFLIES = TagKey.of(RegistryKeys.BIOME, id("has_fireflies"));
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
    public final Block ALGAE_BLOCK = block("algae", new AlgaeBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_GREEN)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .sounds(BlockSoundGroup.GLOW_LICHEN)
            .nonOpaque()
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)));
    public final Item ALGAE_ITEM = item("algae", new AlgaeItem(new Item.Settings(), ALGAE_BLOCK));
    public final RegistryKey<ConfiguredFeature<?, ?>> ALGAE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id("algae"));
    public final RegistryKey<PlacedFeature> ALGAE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("algae"));

    public Exploring(String name) {
        super(name);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    entries.addAfter(Items.COMPASS, METAL_DETECTOR_ITEM);
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
