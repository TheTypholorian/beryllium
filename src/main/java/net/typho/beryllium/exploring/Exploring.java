package net.typho.beryllium.exploring;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.DyeColor;
import net.minecraft.world.biome.Biome;
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
    }
}
