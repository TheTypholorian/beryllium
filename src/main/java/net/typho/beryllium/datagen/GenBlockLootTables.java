package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Items;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.redstone.Redstone;
import net.typho.beryllium.util.BlockFamilyBuilder;

import java.util.concurrent.CompletableFuture;

public class GenBlockLootTables extends FabricBlockLootTableProvider {
    public GenBlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(Building.KILN_BLOCK);

        addDrop(Building.MOSSY_STONE.getBaseBlock(), Blocks.MOSSY_COBBLESTONE);
        addDrop(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL), Blocks.MOSSY_COBBLESTONE_WALL);
        addDrop(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS), Blocks.MOSSY_COBBLESTONE_STAIRS);
        addDrop(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB), Blocks.MOSSY_COBBLESTONE_SLAB);

        addDrop(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL));
        addDrop(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL));
        addDrop(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.CHISELED));

        addDrop(Building.SNOW_BRICKS.getBaseBlock(), block -> drops(block, Items.SNOWBALL, ConstantLootNumberProvider.create(4)));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED), block -> drops(block, Items.SNOWBALL, ConstantLootNumberProvider.create(4)));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL), block -> drops(block, Items.SNOWBALL, ConstantLootNumberProvider.create(4)));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS), block -> drops(block, Items.SNOWBALL, ConstantLootNumberProvider.create(3)));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB), block -> drops(block, Items.SNOWBALL, ConstantLootNumberProvider.create(2)));

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            for (Block block : family.datagen.values()) {
                addDrop(block);
            }
        }

        addDrop(Building.CUT_SANDSTONE_WALL);
        addDrop(Building.CUT_SANDSTONE_STAIRS);
        addDrop(Building.SMOOTH_SANDSTONE_WALL);
        addDrop(Building.CUT_RED_SANDSTONE_WALL);
        addDrop(Building.CUT_RED_SANDSTONE_STAIRS);
        addDrop(Building.SMOOTH_RED_SANDSTONE_WALL);

        addDrop(Exploring.CORRUPTED_END_STONE, block -> drops(block, Blocks.END_STONE));
        addDrop(Exploring.CONGEALED_VOID, this::dropsWithSilkTouch);
        addDrop(Exploring.CORRUPTED_LOG);
        addDrop(Exploring.CORRUPTED_WOOD);
        addDrop(Exploring.STRIPPED_CORRUPTED_LOG);
        addDrop(Exploring.STRIPPED_CORRUPTED_WOOD);
        addDrop(Exploring.CORRUPTED_PLANKS);
        addDrop(Exploring.CORRUPTED_SAPLING);
        addDrop(Exploring.FIREFLY_BOTTLE, this::dropsWithSilkTouch);
        addDrop(Exploring.DAFFODILS, flowerbedDrops(Exploring.DAFFODILS));
        addDrop(Exploring.SCILLA, flowerbedDrops(Exploring.SCILLA));
        addDrop(Exploring.GERANIUMS, flowerbedDrops(Exploring.GERANIUMS));
        addDrop(Exploring.ALGAE_BLOCK, block -> dropsWithSilkTouchOrShears(block, EmptyEntry.builder()));
        addDrop(Exploring.VOID_FIRE, dropsNothing());
        addDrop(Exploring.POINTED_BONE);

        addDrop(Redstone.GOLD_HOPPER_BLOCK);
        addDrop(Redstone.DESTRUCTOR_BLOCK);

        addDrop(Combat.POTION_CAULDRON, Blocks.CAULDRON);
    }
}
