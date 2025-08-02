package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.redstone.Redstone;

import java.util.concurrent.CompletableFuture;

public class GenBlockTags extends FabricTagProvider.BlockTagProvider {
    public GenBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(Building.MOSSY_STONE.getBaseBlock())
                .add(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB))

                .add(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL))
                .add(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB))

                .add(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.CHISELED))

                .add(Building.KILN_BLOCK)

                .add(Redstone.GOLD_HOPPER_BLOCK)

                .add(Combat.POTION_CAULDRON)

                .add(Exploring.POINTED_BONE);

        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(Building.SNOW_BRICKS.getBaseBlock())
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED))
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL))
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL))
                .add(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL));

        getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS));

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB))
                .add(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB))
                .add(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.FLOWERS)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS);

        getOrCreateTagBuilder(Exploring.VOID_FIRE_BASE_BLOCKS)
                .add(Blocks.END_STONE)
                .add(Blocks.OBSIDIAN)
                .add(Blocks.BEDROCK);

        getOrCreateTagBuilder(Exploring.POINTED_BLOCKS)
                .add(Blocks.POINTED_DRIPSTONE)
                .add(Exploring.POINTED_BONE);

        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(Exploring.VOID_FIRE);

        getOrCreateTagBuilder(BlockTags.CAULDRONS)
                .add(Combat.POTION_CAULDRON);
    }
}
