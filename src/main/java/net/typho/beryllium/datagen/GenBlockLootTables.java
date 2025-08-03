package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryWrapper;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.redstone.Redstone;

import java.util.concurrent.CompletableFuture;

public class GenBlockLootTables extends FabricBlockLootTableProvider {
    public GenBlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(Building.KILN_BLOCK);

        addDrop(Building.MOSSY_STONE.getBaseBlock());
        addDrop(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL));
        addDrop(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Building.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL));
        addDrop(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Building.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL));
        addDrop(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Building.SMOOTH_STONE.getVariant(BlockFamily.Variant.CHISELED));

        addDrop(Building.SNOW_BRICKS.getBaseBlock());
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS));
        addDrop(Building.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

        addDrop(Exploring.CORRUPTED_END_STONE);
        addDrop(Exploring.CONGEALED_VOID);
        addDrop(Exploring.CORRUPTED_LOG);
        addDrop(Exploring.CORRUPTED_WOOD);
        addDrop(Exploring.STRIPPED_CORRUPTED_LOG);
        addDrop(Exploring.STRIPPED_CORRUPTED_WOOD);
        addDrop(Exploring.CORRUPTED_PLANKS);
        addDrop(Exploring.CORRUPTED_SAPLING);
        addDrop(Exploring.CORRUPTED_SAPLING);
        addDrop(Exploring.FIREFLY_BOTTLE);
        addDrop(Exploring.DAFFODILS, flowerbedDrops(Exploring.DAFFODILS));
        addDrop(Exploring.SCILLA, flowerbedDrops(Exploring.SCILLA));
        addDrop(Exploring.GERANIUMS, flowerbedDrops(Exploring.GERANIUMS));
        addDrop(Exploring.ALGAE_BLOCK);
        addDrop(Exploring.VOID_FIRE, dropsNothing());
        addDrop(Exploring.POINTED_BONE);

        addDrop(Redstone.GOLD_HOPPER_BLOCK);

        addDrop(Combat.POTION_CAULDRON, Blocks.CAULDRON);
    }
}
