package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.exploring.AlgaeBlock;

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
            context.register(Beryllium.EXPLORING.MAGMA_DELTA_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.DELTA_FEATURE,
                            new DeltaFeatureConfig(Blocks.MAGMA_BLOCK.getDefaultState(), Blocks.MAGMA_BLOCK.getDefaultState(), UniformIntProvider.create(3, 7), UniformIntProvider.create(0, 2))
                    ));
            context.register(Beryllium.EXPLORING.SMALL_BONE_SPIKES_CONFIGURED,
                    new ConfiguredFeature<>(
                            Beryllium.EXPLORING.BONE_SPIKES,
                            new BasaltColumnsFeatureConfig(ConstantIntProvider.create(1), UniformIntProvider.create(1, 4))
                    ));
            context.register(Beryllium.EXPLORING.LARGE_BONE_SPIKES_CONFIGURED,
                    new ConfiguredFeature<>(
                            Beryllium.EXPLORING.BONE_SPIKES,
                            new BasaltColumnsFeatureConfig(UniformIntProvider.create(2, 3), UniformIntProvider.create(5, 10))
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
            context.register(Beryllium.EXPLORING.MAGMA_DELTA_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.MAGMA_DELTA_CONFIGURED),
                    List.of(CountMultilayerPlacementModifier.of(4), BiomePlacementModifier.of())
            ));
            context.register(Beryllium.EXPLORING.SMALL_BONE_SPIKES_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.SMALL_BONE_SPIKES_CONFIGURED),
                    List.of(CountMultilayerPlacementModifier.of(4), BiomePlacementModifier.of())
            ));
            context.register(Beryllium.EXPLORING.LARGE_BONE_SPIKES_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.LARGE_BONE_SPIKES_CONFIGURED),
                    List.of(CountMultilayerPlacementModifier.of(40), BiomePlacementModifier.of())
            ));
        });
        builder.addRegistry(RegistryKeys.BIOME, context -> {
            RegistryEntryLookup<PlacedFeature> featureLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
            RegistryEntryLookup<ConfiguredCarver<?>> carverLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

            SpawnSettings spawnSettings = new SpawnSettings.Builder()
                    .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GHAST, 40, 1, 1))
                    .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.MAGMA_CUBE, 100, 2, 5))
                    .spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2))
                    .build();
            GenerationSettings.LookupBackedBuilder lookupBackedBuilder = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup)
                    .carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
                    .feature(GenerationStep.Feature.SURFACE_STRUCTURES, Beryllium.EXPLORING.MAGMA_DELTA_PLACED)
                    .feature(GenerationStep.Feature.SURFACE_STRUCTURES, Beryllium.EXPLORING.SMALL_BONE_SPIKES_PLACED)
                    .feature(GenerationStep.Feature.SURFACE_STRUCTURES, Beryllium.EXPLORING.LARGE_BONE_SPIKES_PLACED)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_DELTA)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_FIRE)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.PATCH_SOUL_FIRE)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE_EXTRA)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.GLOWSTONE)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, VegetationPlacedFeatures.BROWN_MUSHROOM_NETHER)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, VegetationPlacedFeatures.RED_MUSHROOM_NETHER)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_MAGMA)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, NetherPlacedFeatures.SPRING_CLOSED_DOUBLE)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_GOLD_DELTAS)
                    .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_QUARTZ_DELTAS);
            DefaultBiomeFeatures.addAncientDebris(lookupBackedBuilder);
            context.register(Beryllium.EXPLORING.BONE_FOREST, new Biome.Builder()
                    .precipitation(false)
                    .temperature(1.5F)
                    .downfall(0.0F)
                    .effects(
                            new BiomeEffects.Builder()
                                    .waterColor(4159204)
                                    .waterFogColor(329011)
                                    .fogColor(6840176)
                                    .skyColor(OverworldBiomeCreator.getSkyColor(1.5F))
                                    .particleConfig(new BiomeParticleConfig(ParticleTypes.WHITE_ASH, 0.118093334F))
                                    .loopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                                    .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0))
                                    .additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111))
                                    .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_BASALT_DELTAS))
                                    .build()
                    )
                    .spawnSettings(spawnSettings)
                    .generationSettings(lookupBackedBuilder.build())
                    .build());
        });
    }
}
