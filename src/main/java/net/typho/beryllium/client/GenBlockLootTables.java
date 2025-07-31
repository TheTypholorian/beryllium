package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryWrapper;
import net.typho.beryllium.Beryllium;

import java.util.concurrent.CompletableFuture;

public class GenBlockLootTables extends FabricBlockLootTableProvider {
    public GenBlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(Beryllium.BUILDING.KILN_BLOCK);

        addDrop(Beryllium.BUILDING.MOSSY_STONE.getBaseBlock());
        addDrop(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL));
        addDrop(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL));
        addDrop(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL));
        addDrop(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.CHISELED));

        addDrop(Beryllium.BUILDING.SNOW_BRICKS.getBaseBlock());
        addDrop(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED));
        addDrop(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL));
        addDrop(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Beryllium.EXPLORING.DAFFODILS, flowerbedDrops(Beryllium.EXPLORING.DAFFODILS));
        addDrop(Beryllium.EXPLORING.SCILLA, flowerbedDrops(Beryllium.EXPLORING.SCILLA));
        addDrop(Beryllium.EXPLORING.GERANIUMS, flowerbedDrops(Beryllium.EXPLORING.GERANIUMS));

        addDrop(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK);

        addDrop(Beryllium.EXPLORING.VOID_FIRE, dropsNothing());

        addDrop(Beryllium.COMBAT.POTION_CAULDRON, Blocks.CAULDRON);

        addDrop(Beryllium.EXPLORING.POINTED_BONE);
    }
}
