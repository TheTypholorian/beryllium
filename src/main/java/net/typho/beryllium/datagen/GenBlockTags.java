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
                .add(Exploring.POINTED_BONE)
                .add(Exploring.ONYX_ORE);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (!family.tags.isEmpty()) {
                System.out.println("Block tags for " + family.prefix);
            }

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
                System.out.println("Wall block tags for " + family.prefix);
                walls.add(family.datagen.get(BlockFamily.Variant.WALL));
            }
        }

        FabricTagBuilder fences = getOrCreateTagBuilder(BlockTags.FENCES);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.FENCE)) {
                System.out.println("Wall block tags for " + family.prefix);
                fences.add(family.datagen.get(BlockFamily.Variant.FENCE));
            }
        }

        FabricTagBuilder fenceGates = getOrCreateTagBuilder(BlockTags.FENCE_GATES);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.FENCE_GATE)) {
                System.out.println("Wall block tags for " + family.prefix);
                fenceGates.add(family.datagen.get(BlockFamily.Variant.FENCE_GATE));
            }
        }

        FabricTagBuilder stairs = getOrCreateTagBuilder(BlockTags.STAIRS);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.STAIRS)) {
                System.out.println("Stair block tags for " + family.prefix);
                stairs.add(family.datagen.get(BlockFamily.Variant.STAIRS));
            }
        }

        FabricTagBuilder slabs = getOrCreateTagBuilder(BlockTags.SLABS);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            if (family.datagen.containsKey(BlockFamily.Variant.SLAB)) {
                System.out.println("Slab block tags for " + family.prefix);
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

        getOrCreateTagBuilder(Exploring.CHORUS_PLANTABLE)
                .add(Blocks.END_STONE)
                .add(Exploring.ONYX_ORE)
                .add(Exploring.CORRUPTED_END_STONE);

        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(Exploring.VOID_FIRE);

        getOrCreateTagBuilder(BlockTags.CAULDRONS)
                .add(Combat.POTION_CAULDRON);
    }
}
