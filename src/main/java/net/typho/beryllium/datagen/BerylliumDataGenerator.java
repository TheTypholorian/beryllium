package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.NoiseThresholdCountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.exploring.AlgaeBlock;

import java.util.List;

public class BerylliumDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        pack.addProvider(GenItemTags::new);
        pack.addProvider(GenBlockTags::new);
        pack.addProvider(GenStructureTags::new);
        pack.addProvider(GenBiomeTags::new);
        pack.addProvider(GenDamageTypeTags::new);
        pack.addProvider(GenModels::new);
        pack.addProvider(GenRecipes::new);
        pack.addProvider(GenBlockLootTables::new);
        pack.addProvider(GenRegistries::new);
    }

    public static DataPool.Builder<BlockState> allFlowerbedStates(BlockState state) {
        DataPool.Builder<BlockState> daffodils = DataPool.builder();

        for (int i = 1; i <= 4; i++) {
            for (Direction direction : Direction.Type.HORIZONTAL) {
                daffodils.add(state.with(FlowerbedBlock.FLOWER_AMOUNT, i).with(FlowerbedBlock.FACING, direction), 1);
            }
        }

        return daffodils;
    }

    @Override
    public void buildRegistry(RegistryBuilder builder) {
        builder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, context -> {
            context.register(Beryllium.EXPLORING.SWAMP_ALGAE_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.RANDOM_PATCH,
                            new RandomPatchFeatureConfig(
                                    50, 10, 1, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Beryllium.EXPLORING.ALGAE_BLOCK.getDefaultState().with(Properties.DOWN, true).with(AlgaeBlock.GENERATED, true))))
                            )
                    ));
            context.register(Beryllium.EXPLORING.RIVER_ALGAE_CONFIGURED,
                    new ConfiguredFeature<>(
                            Beryllium.EXPLORING.RIVER_ALGAE_FEATURE,
                            new DefaultFeatureConfig()
                    ));
            context.register(Beryllium.EXPLORING.DAFFODILS_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.FLOWER,
                            new RandomPatchFeatureConfig(
                                    96, 6, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(allFlowerbedStates(Beryllium.EXPLORING.DAFFODILS.getDefaultState()))))
                            )
                    ));
            context.register(Beryllium.EXPLORING.SCILLA_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.FLOWER,
                            new RandomPatchFeatureConfig(
                                    96, 6, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(allFlowerbedStates(Beryllium.EXPLORING.SCILLA.getDefaultState()))))
                            )
                    ));
            context.register(Beryllium.EXPLORING.GERANIUMS_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.FLOWER,
                            new RandomPatchFeatureConfig(
                                    96, 6, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(allFlowerbedStates(Beryllium.EXPLORING.GERANIUMS.getDefaultState()))))
                            )
                    ));
        });
        builder.addRegistry(RegistryKeys.PLACED_FEATURE, context -> {
            context.register(Beryllium.EXPLORING.SWAMP_ALGAE_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.SWAMP_ALGAE_CONFIGURED),
                    List.of(CountPlacementModifier.of(4), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Beryllium.EXPLORING.RIVER_ALGAE_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.RIVER_ALGAE_CONFIGURED),
                    List.of(CountPlacementModifier.of(40), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Beryllium.EXPLORING.DAFFODILS_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.DAFFODILS_CONFIGURED),
                    List.of(NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Beryllium.EXPLORING.SCILLA_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.SCILLA_CONFIGURED),
                    List.of(NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Beryllium.EXPLORING.GERANIUMS_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.GERANIUMS_CONFIGURED),
                    List.of(NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())
            ));
        });
        builder.addRegistry(RegistryKeys.BIOME, context -> {
        });
    }
}
