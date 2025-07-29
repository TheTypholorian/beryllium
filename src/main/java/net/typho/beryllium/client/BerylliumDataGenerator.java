package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.NoiseThresholdCountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.building.kiln.KilnRecipe;
import net.typho.beryllium.exploring.AlgaeBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    }

    public static class GenRegistries extends FabricDynamicRegistryProvider {
        public GenRegistries(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
            entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
        }

        @Override
        public String getName() {
            return "";
        }
    }

    public static class GenItemTags extends FabricTagProvider.ItemTagProvider {
        public GenItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
            super(output, completableFuture, blockTagProvider);
        }

        public GenItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(ItemTags.ARROWS)
                    .add(Beryllium.COMBAT.DIAMOND_ARROW)
                    .add(Beryllium.COMBAT.IRON_ARROW)
                    .add(Beryllium.COMBAT.FLAMING_ARROW)
                    .add(Beryllium.COMBAT.COPPER_ARROW);

            getOrCreateTagBuilder(ItemTags.FLOWERS)
                    .add(Beryllium.EXPLORING.DAFFODILS.asItem())
                    .add(Beryllium.EXPLORING.SCILLA.asItem())
                    .add(Beryllium.EXPLORING.GERANIUMS.asItem());
        }
    }

    public static class GenBlockTags extends FabricTagProvider.BlockTagProvider {
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

                    .add(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK);

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

            getOrCreateTagBuilder(BlockTags.FIRE)
                    .add(Beryllium.EXPLORING.VOID_FIRE);
        }
    }

    public static class GenStructureTags extends FabricTagProvider<Structure> {
        public GenStructureTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.STRUCTURE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(Beryllium.EXPLORING.ON_BASTION_MAPS)
                    .add(StructureKeys.BASTION_REMNANT);
            getOrCreateTagBuilder(Beryllium.EXPLORING.ON_FORTRESS_MAPS)
                    .add(StructureKeys.FORTRESS);
            getOrCreateTagBuilder(Beryllium.EXPLORING.SPAWN_KEY)
                    .add(StructureKeys.VILLAGE_PLAINS)
                    .add(StructureKeys.VILLAGE_DESERT)
                    .add(StructureKeys.VILLAGE_SAVANNA)
                    .add(StructureKeys.VILLAGE_SNOWY)
                    .add(StructureKeys.VILLAGE_TAIGA);
        }
    }

    public static class GenBiomeTags extends FabricTagProvider<Biome> {
        public GenBiomeTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.BIOME, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(Beryllium.EXPLORING.HAS_FIREFLIES)
                    .add(BiomeKeys.BIRCH_FOREST)
                    .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
                    .add(BiomeKeys.SWAMP)
                    .add(BiomeKeys.MANGROVE_SWAMP);
            getOrCreateTagBuilder(Beryllium.EXPLORING.BIRCH_TAG)
                    .add(BiomeKeys.BIRCH_FOREST)
                    .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
            getOrCreateTagBuilder(Beryllium.EXPLORING.SPRUCE_TAG)
                    .add(BiomeKeys.TAIGA)
                    .add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA)
                    .add(BiomeKeys.OLD_GROWTH_PINE_TAIGA);
            getOrCreateTagBuilder(Beryllium.EXPLORING.OAK_TAG)
                    .add(BiomeKeys.FOREST);
        }
    }

    public static class GenDamageTypeTags extends FabricTagProvider<DamageType> {
        public GenDamageTypeTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(TagKey.of(registryRef, Beryllium.EXPLORING.id("kinetic")))
                    .add(DamageTypes.FLY_INTO_WALL);
        }
    }

    public static class GenModels extends FabricModelProvider {
        public GenModels(FabricDataOutput output) {
            super(output);
        }

        public static void family(BlockStateModelGenerator gen, BlockFamily family) {
            BlockStateModelGenerator.BlockTexturePool pool = gen.registerCubeAllModelTexturePool(family.getBaseBlock());
            pool.family(family);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator gen) {
            family(gen, Beryllium.BUILDING.MOSSY_STONE);
            family(gen, Beryllium.BUILDING.CRACKED_STONE_BRICKS);
            family(gen, Beryllium.BUILDING.SMOOTH_STONE);
            family(gen, Beryllium.BUILDING.SNOW_BRICKS);

            gen.registerFlowerbed(Beryllium.EXPLORING.DAFFODILS);
            gen.registerFlowerbed(Beryllium.EXPLORING.SCILLA);
            gen.registerFlowerbed(Beryllium.EXPLORING.GERANIUMS);
            gen.registerWallPlant(Beryllium.EXPLORING.ALGAE_BLOCK);

            Identifier goldHopper = ModelIds.getBlockModelId(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK);
            Identifier goldHopperSide = ModelIds.getBlockSubModelId(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK, "_side");
            gen.registerItemModel(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK.asItem());
            gen.blockStateCollector
                    .accept(
                            VariantsBlockStateSupplier.create(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK)
                                    .coordinate(
                                            BlockStateVariantMap.create(Properties.HOPPER_FACING)
                                                    .register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopper))
                                                    .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide))
                                                    .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                                    .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                                    .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                    )
                    );

            List<Identifier> voidFireFloor = gen.getFireFloorModels(Beryllium.EXPLORING.VOID_FIRE);
            List<Identifier> voidFireSide = gen.getFireSideModels(Beryllium.EXPLORING.VOID_FIRE);
            gen.blockStateCollector
                    .accept(
                            MultipartBlockStateSupplier.create(Beryllium.EXPLORING.VOID_FIRE)
                                    .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireFloor, v -> v))
                                    .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v))
                                    .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R90)))
                                    .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R180)))
                                    .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R270)))
                    );
        }

        @Override
        public void generateItemModels(ItemModelGenerator gen) {
            gen.register(Beryllium.COMBAT.DIAMOND_ARROW, Models.GENERATED);
            gen.register(Beryllium.COMBAT.IRON_ARROW, Models.GENERATED);
            gen.register(Beryllium.COMBAT.FLAMING_ARROW, Models.GENERATED);
            gen.register(Beryllium.COMBAT.COPPER_ARROW, Models.GENERATED);
            gen.register(Beryllium.BUILDING.MAGIC_WAND_ITEM, Models.GENERATED);
            gen.register(Beryllium.EXPLORING.FIREFLY_BOTTLE.asItem(), Models.GENERATED);
            gen.register(Beryllium.FOOD.CROISSANT, Models.GENERATED);
        }
    }

    public static class GenRecipes extends FabricRecipeProvider {
        public GenRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        public static void arrow(RecipeExporter exporter, Item result, Item tip, String tipCriterion) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result, 4)
                    .input('#', Items.STICK)
                    .input('X', tip)
                    .input('Y', Items.FEATHER)
                    .pattern("X")
                    .pattern("#")
                    .pattern("Y")
                    .criterion("has_feather", conditionsFromItem(Items.FEATHER))
                    .criterion(tipCriterion, conditionsFromItem(tip))
                    .offerTo(exporter);
        }

        public static void glaive(RecipeExporter exporter, Item result, Item tip, String tipCriterion) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result, 1)
                    .input('#', Items.STICK)
                    .input('X', tip)
                    .pattern("  X")
                    .pattern(" # ")
                    .pattern("#  ")
                    .criterion(tipCriterion, conditionsFromItem(tip))
                    .offerTo(exporter);
        }

        public static void scythe(RecipeExporter exporter, Item result, Item tip, String tipCriterion) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result, 1)
                    .input('#', Items.STICK)
                    .input('X', tip)
                    .pattern("XXX")
                    .pattern(" #X")
                    .pattern("#  ")
                    .criterion(tipCriterion, conditionsFromItem(tip))
                    .offerTo(exporter);
        }

        public static void wall(RecipeExporter gen, Block result, Block ingredient) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result, 6)
                    .pattern("AAA")
                    .pattern("AAA")
                    .input('A', ingredient)
                    .criterion("has_" + net.minecraft.registry.Registries.BLOCK.getId(ingredient).getPath(), FabricRecipeProvider.conditionsFromItem(ingredient))
                    .offerTo(gen, net.minecraft.registry.Registries.BLOCK.getId(result));
        }

        public static void offerFiring(
                RecipeExporter exporter,
                List<ItemConvertible> inputs,
                RecipeCategory category,
                ItemConvertible output,
                float experience,
                int cookingTime,
                String group
        ) {
            offerMultipleOptions(exporter, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_firing");
        }

        public static void firingStone(RecipeExporter gen, List<ItemConvertible> inputs, ItemConvertible output, String group) {
            offerFiring(gen, inputs, RecipeCategory.BUILDING_BLOCKS, output, 0.1f, 100, group);
        }

        @Override
        public void generate(RecipeExporter exporter) {
            arrow(exporter, Beryllium.COMBAT.DIAMOND_ARROW, Items.DIAMOND, "has_diamond");
            arrow(exporter, Beryllium.COMBAT.IRON_ARROW, Items.IRON_INGOT, "has_iron_ingot");
            arrow(exporter, Beryllium.COMBAT.FLAMING_ARROW, Items.FIRE_CHARGE, "has_fire_charge");
            arrow(exporter, Beryllium.COMBAT.COPPER_ARROW, Items.COPPER_INGOT, "has_copper_ingot");

            offerNetheriteUpgradeRecipe(exporter, Beryllium.COMBAT.DIAMOND_GLAIVE, RecipeCategory.COMBAT, Beryllium.COMBAT.NETHERITE_GLAIVE);
            glaive(exporter, Beryllium.COMBAT.DIAMOND_GLAIVE, Items.DIAMOND, "has_diamond");
            glaive(exporter, Beryllium.COMBAT.IRON_GLAIVE, Items.IRON_INGOT, "has_iron_ingot");
            glaive(exporter, Beryllium.COMBAT.GOLDEN_GLAIVE, Items.GOLD_INGOT, "has_gold_ingot");

            offerNetheriteUpgradeRecipe(exporter, Beryllium.COMBAT.DIAMOND_SCYTHE, RecipeCategory.COMBAT, Beryllium.COMBAT.NETHERITE_SCYTHE);
            scythe(exporter, Beryllium.COMBAT.DIAMOND_SCYTHE, Items.DIAMOND, "has_diamond");
            scythe(exporter, Beryllium.COMBAT.IRON_SCYTHE, Items.IRON_INGOT, "has_iron_ingot");
            scythe(exporter, Beryllium.COMBAT.GOLDEN_SCYTHE, Items.GOLD_INGOT, "has_gold_ingot");

            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Beryllium.BUILDING.KILN_BLOCK.asItem(), 1)
                    .pattern("AAA")
                    .pattern("A A")
                    .pattern("AAA")
                    .input('A', Items.BRICK)
                    .criterion("has_brick", FabricRecipeProvider.conditionsFromItem(Items.BRICK))
                    .offerTo(exporter, Beryllium.BUILDING.id("kiln"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, Beryllium.EXPLORING.METAL_DETECTOR_ITEM, 1)
                    .pattern(" C ")
                    .pattern("CRC")
                    .pattern(" C ")
                    .input('C', Items.COPPER_INGOT)
                    .input('R', Items.REDSTONE)
                    .criterion("has_redstone", FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                    .offerTo(exporter, Beryllium.EXPLORING.id("metal_detector"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.LODESTONE, 1)
                    .pattern("AAA")
                    .pattern("ABA")
                    .pattern("AAA")
                    .input('A', Items.CHISELED_STONE_BRICKS)
                    .input('B', Items.IRON_INGOT)
                    .criterion("has_chiseled_stone_bricks", FabricRecipeProvider.conditionsFromItem(Items.CHISELED_STONE_BRICKS))
                    .offerTo(exporter, Identifier.ofVanilla("lodestone"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, Beryllium.FOOD.CROISSANT, 1)
                    .pattern("   ")
                    .pattern(" C ")
                    .pattern("WWW")
                    .input('C', Items.COCOA_BEANS)
                    .input('W', Items.WHEAT)
                    .criterion("has_cocoa_beans", FabricRecipeProvider.conditionsFromItem(Items.COCOA_BEANS))
                    .offerTo(exporter, Beryllium.FOOD.id("croissant"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.DISPENSER, 1)
                    .pattern("###")
                    .pattern("#X#")
                    .pattern("#R#")
                    .input('#', Items.COBBLESTONE)
                    .input('X', Items.STRING)
                    .input('R', Items.REDSTONE)
                    .criterion("has_redstone", FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                    .offerTo(exporter, Identifier.ofVanilla("dispenser"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.SADDLE, 1)
                    .pattern(" L ")
                    .pattern("LIL")
                    .input('L', Items.LEATHER)
                    .input('I', Items.IRON_INGOT)
                    .criterion("has_leather", FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                    .offerTo(exporter, Identifier.ofVanilla("saddle"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.LEATHER_HORSE_ARMOR, 1)
                    .pattern("M  ")
                    .pattern("MLM")
                    .pattern("M M")
                    .input('M', Items.LEATHER)
                    .input('L', Items.LEATHER)
                    .criterion("has_leather", FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                    .offerTo(exporter, Identifier.ofVanilla("leather_horse_armor"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.IRON_HORSE_ARMOR, 1)
                    .pattern("M  ")
                    .pattern("MLM")
                    .pattern("M M")
                    .input('M', Items.IRON_INGOT)
                    .input('L', Items.LEATHER)
                    .criterion("has_iron_ingot", FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(exporter, Identifier.ofVanilla("iron_horse_armor"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.GOLDEN_HORSE_ARMOR, 1)
                    .pattern("M  ")
                    .pattern("MLM")
                    .pattern("M M")
                    .input('M', Items.GOLD_INGOT)
                    .input('L', Items.LEATHER)
                    .criterion("has_gold_ingot", FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(exporter, Identifier.ofVanilla("golden_horse_armor"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.DIAMOND_HORSE_ARMOR, 1)
                    .pattern("M  ")
                    .pattern("MLM")
                    .pattern("M M")
                    .input('M', Items.DIAMOND)
                    .input('L', Items.LEATHER)
                    .criterion("has_diamond", FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
                    .offerTo(exporter, Identifier.ofVanilla("diamond_horse_armor"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Beryllium.REDSTONE.GOLD_HOPPER_BLOCK, 1)
                    .pattern("GRG")
                    .pattern("GCG")
                    .pattern(" G ")
                    .input('G', Items.GOLD_INGOT)
                    .input('R', Items.REDSTONE)
                    .input('C', Items.CHEST)
                    .criterion("has_gold_ingot", FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                    .offerTo(exporter, Beryllium.REDSTONE.id("gold_hopper"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.SLIME_BALL, 1)
                    .input(Items.SUGAR)
                    .input(Items.LIME_DYE)
                    .criterion("has_lime_dye", FabricRecipeProvider.conditionsFromItem(Items.LIME_DYE))
                    .offerTo(exporter, Beryllium.EXPLORING.id("slimeball"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.STRING, 4)
                    .input(ItemTags.WOOL)
                    .input(Items.SHEARS)
                    .criterion("has_wool", FabricRecipeProvider.conditionsFromTag(ItemTags.WOOL))
                    .offerTo(exporter, Beryllium.EXPLORING.id("string_from_wool"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.STRING, 2)
                    .input(ItemTags.WOOL_CARPETS)
                    .input(Items.SHEARS)
                    .criterion("has_carpet", FabricRecipeProvider.conditionsFromTag(ItemTags.WOOL_CARPETS))
                    .offerTo(exporter, Beryllium.EXPLORING.id("string_from_carpet"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.GRAVEL, 1)
                    .input(Items.WATER_BUCKET)
                    .input(Items.COBBLESTONE)
                    .criterion("has_water", FabricRecipeProvider.conditionsFromItem(Items.WATER_BUCKET))
                    .offerTo(exporter, Beryllium.EXPLORING.id("gravel_from_washing"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.SAND, 1)
                    .input(Items.WATER_BUCKET)
                    .input(Items.GRAVEL)
                    .criterion("has_water", FabricRecipeProvider.conditionsFromItem(Items.WATER_BUCKET))
                    .offerTo(exporter, Beryllium.EXPLORING.id("sand_from_washing"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.IRON_NUGGET, 1)
                    .input(Items.WATER_BUCKET)
                    .input(Items.SAND)
                    .criterion("has_water", FabricRecipeProvider.conditionsFromItem(Items.WATER_BUCKET))
                    .offerTo(exporter, Beryllium.EXPLORING.id("iron_nugget_from_washing"));

            offerSmelting(exporter, List.of(Items.SUGAR_CANE), RecipeCategory.MISC, Items.LIME_DYE, 1, 200, "lime_dye");

            offerSmelting(exporter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.2f, 100, "leather");
            offerMultipleOptions(exporter, RecipeSerializer.SMOKING, SmokingRecipe::new, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.2f, 100, "leather", "_from_smoking");

            BlockFamilies.STONE.getVariants().forEach((variant, stone) -> {
                Block mossy = Beryllium.BUILDING.MOSSY_STONE.getVariant(variant);

                if (mossy != null) {
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, mossy)
                            .input(stone)
                            .input(Blocks.VINE)
                            .group("mossy_stone")
                            .criterion("has_vine", conditionsFromItem(Blocks.VINE))
                            .offerTo(exporter, convertBetween(mossy, Blocks.VINE));
                }
            });
            firingStone(exporter, List.of(Blocks.COBBLESTONE), Blocks.STONE, "stone");
            BlockFamilies.STONE.getVariants().forEach((variant, stone) -> {
                Block cobble = BlockFamilies.COBBLESTONE.getVariant(variant);

                if (cobble != null) {
                    firingStone(exporter, List.of(cobble), stone, "stone");
                }
            });
            firingStone(exporter, List.of(Blocks.STONE), Blocks.SMOOTH_STONE, "stone");
            Beryllium.BUILDING.SMOOTH_STONE.getVariants().forEach((variant, smooth) -> {
                Block stone = BlockFamilies.STONE.getVariant(variant);

                if (stone != null) {
                    firingStone(exporter, List.of(stone), smooth, "smooth_stone");
                }
            });

            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Items.CLAY_BALL), RecipeCategory.MISC, Items.BRICK, 0.3f, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_clay_ball", conditionsFromItem(Items.CLAY_BALL))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.CLAY), RecipeCategory.BUILDING_BLOCKS, Blocks.TERRACOTTA.asItem(), 0.35f, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_clay_block", conditionsFromItem(Blocks.CLAY))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.fromTag(ItemTags.LOGS_THAT_BURN), RecipeCategory.MISC, Items.CHARCOAL, 0.15f, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_log", conditionsFromTag(ItemTags.LOGS_THAT_BURN))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.NETHERRACK), RecipeCategory.MISC, Items.NETHER_BRICK, 0.1f, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_netherrack", conditionsFromItem(Blocks.NETHERRACK))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.fromTag(ItemTags.SMELTS_TO_GLASS), RecipeCategory.BUILDING_BLOCKS, Blocks.GLASS, 0.1f, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_smelts_to_glass", conditionsFromTag(ItemTags.SMELTS_TO_GLASS))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.SANDSTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_SANDSTONE.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_sandstone", conditionsFromItem(Blocks.SANDSTONE))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.RED_SANDSTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_RED_SANDSTONE.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_red_sandstone", conditionsFromItem(Blocks.RED_SANDSTONE))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.QUARTZ_BLOCK), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_QUARTZ.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_quartz_block", conditionsFromItem(Blocks.QUARTZ_BLOCK))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.BLACK_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BLACK_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_black_terracotta", conditionsFromItem(Blocks.BLACK_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.BLUE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_blue_terracotta", conditionsFromItem(Blocks.BLUE_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.BROWN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BROWN_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_brown_terracotta", conditionsFromItem(Blocks.BROWN_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.CYAN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.CYAN_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_cyan_terracotta", conditionsFromItem(Blocks.CYAN_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.GRAY_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_gray_terracotta", conditionsFromItem(Blocks.GRAY_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.GREEN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.GREEN_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_green_terracotta", conditionsFromItem(Blocks.GREEN_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.LIGHT_BLUE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_light_blue_terracotta", conditionsFromItem(Blocks.LIGHT_BLUE_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.LIGHT_GRAY_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_light_gray_terracotta", conditionsFromItem(Blocks.LIGHT_GRAY_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.LIME_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIME_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_lime_terracotta", conditionsFromItem(Blocks.LIME_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.MAGENTA_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.MAGENTA_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_magenta_terracotta", conditionsFromItem(Blocks.MAGENTA_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.ORANGE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.ORANGE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_orange_terracotta", conditionsFromItem(Blocks.ORANGE_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.PINK_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.PINK_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_pink_terracotta", conditionsFromItem(Blocks.PINK_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.PURPLE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.PURPLE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_purple_terracotta", conditionsFromItem(Blocks.PURPLE_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.RED_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.RED_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_red_terracotta", conditionsFromItem(Blocks.RED_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.WHITE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.WHITE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_white_terracotta", conditionsFromItem(Blocks.WHITE_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(
                            Ingredient.ofItems(Blocks.YELLOW_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.YELLOW_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                    )
                    .criterion("has_yellow_terracotta", conditionsFromItem(Blocks.YELLOW_TERRACOTTA))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.BASALT), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_BASALT, 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_basalt", conditionsFromItem(Blocks.BASALT))
                    .offerTo(exporter);
            CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.COBBLED_DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE, 0.1F, 100, Beryllium.BUILDING.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                    .criterion("has_cobbled_deepslate", conditionsFromItem(Blocks.COBBLED_DEEPSLATE))
                    .offerTo(exporter);

            generateFamily(exporter, Beryllium.BUILDING.MOSSY_STONE, FeatureSet.empty());
            generateFamily(exporter, Beryllium.BUILDING.CRACKED_STONE_BRICKS, FeatureSet.empty());
        }
    }

    public static class GenBlockLootTables extends FabricBlockLootTableProvider {
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
        }
    }
}
