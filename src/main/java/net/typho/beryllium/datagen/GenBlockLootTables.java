package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.entry.EmptyEntry;
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

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            for (Block block : family.datagen.values()) {
                addDrop(block);
            }
        }

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
