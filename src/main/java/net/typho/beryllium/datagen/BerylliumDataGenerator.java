package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.BiasedToBottomIntProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.CherryTrunkPlacer;
import net.typho.beryllium.exploring.AlgaeBlock;
import net.typho.beryllium.exploring.Exploring;

import java.util.List;

@SuppressWarnings("deprecation")
public class BerylliumDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        pack.addProvider(GenItemTags::new);
        pack.addProvider(GenBlockTags::new);
        pack.addProvider(GenStructureTags::new);
        pack.addProvider(GenBiomeTags::new);
        pack.addProvider(GenArmorTrimPatternTags::new);
        pack.addProvider(GenArmorTrimMaterialTags::new);
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
            context.register(Exploring.SWAMP_ALGAE_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.RANDOM_PATCH,
                            new RandomPatchFeatureConfig(
                                    50, 10, 1, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Exploring.ALGAE_BLOCK.getDefaultState().with(Properties.DOWN, true).with(AlgaeBlock.GENERATED, true))))
                            )
                    ));
            context.register(Exploring.RIVER_ALGAE_CONFIGURED,
                    new ConfiguredFeature<>(
                            Exploring.RIVER_ALGAE_FEATURE,
                            new DefaultFeatureConfig()
                    ));
            context.register(Exploring.DAFFODILS_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.FLOWER,
                            new RandomPatchFeatureConfig(
                                    96, 6, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(allFlowerbedStates(Exploring.DAFFODILS.getDefaultState()))))
                            )
                    ));
            context.register(Exploring.SCILLA_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.FLOWER,
                            new RandomPatchFeatureConfig(
                                    96, 6, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(allFlowerbedStates(Exploring.SCILLA.getDefaultState()))))
                            )
                    ));
            context.register(Exploring.GERANIUMS_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.FLOWER,
                            new RandomPatchFeatureConfig(
                                    96, 6, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(allFlowerbedStates(Exploring.GERANIUMS.getDefaultState()))))
                            )
                    ));
            context.register(Exploring.MAGMA_DELTA_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.DELTA_FEATURE,
                            new DeltaFeatureConfig(Blocks.MAGMA_BLOCK.getDefaultState(), Blocks.MAGMA_BLOCK.getDefaultState(), UniformIntProvider.create(3, 7), UniformIntProvider.create(0, 2))
                    ));
            context.register(Exploring.BONE_SPIKES_CONFIGURED,
                    new ConfiguredFeature<>(
                            Exploring.BONE_SPIKES,
                            new BasaltColumnsFeatureConfig(ConstantIntProvider.create(1), UniformIntProvider.create(1, 3))
                    ));
            context.register(Exploring.CORRUPTED_TREE_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.TREE,
                            new TreeFeatureConfig.Builder(
                                    BlockStateProvider.of(Exploring.CORRUPTED_LOG),
                                    new CherryTrunkPlacer(5, 6, 5, UniformIntProvider.create(1, 3), UniformIntProvider.create(3, 7), UniformIntProvider.create(-4, -3), UniformIntProvider.create(-1, 0)),

                                    BlockStateProvider.of(Exploring.CONGEALED_VOID),
                                    new CherryFoliagePlacer(ConstantIntProvider.create(6), ConstantIntProvider.create(0), ConstantIntProvider.create(4), 0.25F, 0.5F, 0, 0),

                                    new TwoLayersFeatureSize(1, 0, 2)
                            ).dirtProvider(BlockStateProvider.of(Blocks.END_STONE)).build()
                    ));
        });
        builder.addRegistry(RegistryKeys.PLACED_FEATURE, context -> {
            context.register(Exploring.SWAMP_ALGAE_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.SWAMP_ALGAE_CONFIGURED),
                    List.of(CountPlacementModifier.of(4), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Exploring.RIVER_ALGAE_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.RIVER_ALGAE_CONFIGURED),
                    List.of(CountPlacementModifier.of(40), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Exploring.DAFFODILS_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.DAFFODILS_CONFIGURED),
                    List.of(NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Exploring.SCILLA_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.SCILLA_CONFIGURED),
                    List.of(NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Exploring.GERANIUMS_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.GERANIUMS_CONFIGURED),
                    List.of(NoiseThresholdCountPlacementModifier.of(-0.8, 5, 10), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of())
            ));
            context.register(Exploring.MAGMA_DELTA_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.MAGMA_DELTA_CONFIGURED),
                    List.of(CountMultilayerPlacementModifier.of(4), BiomePlacementModifier.of())
            ));
            context.register(Exploring.BONE_SPIKES_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.BONE_SPIKES_CONFIGURED),
                    List.of(
                            CountPlacementModifier.of(BiasedToBottomIntProvider.create(0, 4)),
                            SquarePlacementModifier.of(),
                            PlacedFeatures.FOUR_ABOVE_AND_BELOW_RANGE,
                            BiomePlacementModifier.of()
                    )
            ));
            context.register(Exploring.CORRUPTED_TREE_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Exploring.CORRUPTED_TREE_CONFIGURED),
                    VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                            PlacedFeatures.createCountExtraModifier(2, 0.1f, 2),
                            Exploring.CORRUPTED_SAPLING
                    )
            ));
        });
        builder.addRegistry(RegistryKeys.BIOME, context -> {
            RegistryEntryLookup<PlacedFeature> featureLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
            RegistryEntryLookup<ConfiguredCarver<?>> carverLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

            SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
            DefaultBiomeFeatures.addEndMobs(spawnSettings);

            context.register(Exploring.CORRUPTED_FOREST, new Biome.Builder()
                    .precipitation(false)
                    .temperature(0.5F)
                    .downfall(0.5F)
                    .effects(new BiomeEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(10518688).skyColor(0).moodSound(BiomeMoodSound.CAVE).build())
                    .spawnSettings(spawnSettings.build())
                    .generationSettings(new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup)
                            .feature(GenerationStep.Feature.VEGETAL_DECORATION, Exploring.CORRUPTED_TREE_PLACED)
                            .feature(GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN)
                            .feature(GenerationStep.Feature.VEGETAL_DECORATION, EndPlacedFeatures.CHORUS_PLANT)
                            .build())
                    .build());
        });
    }
}
