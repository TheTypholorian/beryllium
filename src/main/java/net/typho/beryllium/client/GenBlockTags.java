package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.typho.beryllium.Beryllium;

import java.util.concurrent.CompletableFuture;

public class GenBlockTags extends FabricTagProvider.BlockTagProvider {
    public GenBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(Beryllium.BUILDING.MOSSY_STONE.getBaseBlock())
                .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB))

                .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB))

                .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.CHISELED))

                .add(Beryllium.BUILDING.KILN_BLOCK)

                .add(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK)

                .add(Beryllium.COMBAT.POTION_CAULDRON)

                .add(Beryllium.EXPLORING.POINTED_BONE);

        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(Beryllium.BUILDING.SNOW_BRICKS.getBaseBlock())
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED))
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL))
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL));

        getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS))
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS));

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB))
                .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB))
                .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(Beryllium.EXPLORING.DAFFODILS)
                .add(Beryllium.EXPLORING.SCILLA)
                .add(Beryllium.EXPLORING.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
                .add(Beryllium.EXPLORING.DAFFODILS)
                .add(Beryllium.EXPLORING.SCILLA)
                .add(Beryllium.EXPLORING.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(Beryllium.EXPLORING.DAFFODILS)
                .add(Beryllium.EXPLORING.SCILLA)
                .add(Beryllium.EXPLORING.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.FLOWERS)
                .add(Beryllium.EXPLORING.DAFFODILS)
                .add(Beryllium.EXPLORING.SCILLA)
                .add(Beryllium.EXPLORING.GERANIUMS);

        getOrCreateTagBuilder(Beryllium.EXPLORING.VOID_FIRE_BASE_BLOCKS)
                .add(Blocks.END_STONE)
                .add(Blocks.OBSIDIAN)
                .add(Blocks.BEDROCK);

        getOrCreateTagBuilder(Beryllium.EXPLORING.POINTED_BLOCKS)
                .add(Blocks.POINTED_DRIPSTONE)
                .add(Beryllium.EXPLORING.POINTED_BONE);

        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(Beryllium.EXPLORING.VOID_FIRE);

        getOrCreateTagBuilder(BlockTags.CAULDRONS)
                .add(Beryllium.COMBAT.POTION_CAULDRON);
    }
}
