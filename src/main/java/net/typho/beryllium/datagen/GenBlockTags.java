package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.util.BlockFamilyBuilder;

import java.util.concurrent.CompletableFuture;

public class GenBlockTags extends FabricTagProvider.BlockTagProvider {
    public GenBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(Building.KILN_BLOCK)
                .add(Combat.POTION_CAULDRON)
                .add(Exploring.POINTED_BONE);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            for (TagKey<Block> tag : family.tags) {
                FabricTagBuilder builder = getOrCreateTagBuilder(tag);

                for (Block block : family.datagen.values()) {
                    builder.add(block);
                }
            }
        }

        FabricTagBuilder walls = getOrCreateTagBuilder(BlockTags.WALLS);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.WALL)) {
                walls.add(family.datagen.get(BlockFamily.Variant.WALL));
            }
        }

        FabricTagBuilder stairs = getOrCreateTagBuilder(BlockTags.STAIRS);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.STAIRS)) {
                stairs.add(family.datagen.get(BlockFamily.Variant.STAIRS));
            }
        }

        FabricTagBuilder slabs = getOrCreateTagBuilder(BlockTags.SLABS);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.SLAB)) {
                slabs.add(family.datagen.get(BlockFamily.Variant.SLAB));
            }
        }

        getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS);

        getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS)
                .add(Exploring.ALGAE_BLOCK);

        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(Exploring.DAFFODILS)
                .add(Exploring.SCILLA)
                .add(Exploring.GERANIUMS)
                .add(Exploring.ALGAE_BLOCK);

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
