package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
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

        pack.addProvider(ItemTags::new);
        pack.addProvider(Models::new);
        pack.addProvider(Recipes::new);
        pack.addProvider(BlockLootTables::new);
        pack.addProvider(BlockTags::new);
        pack.addProvider(StructureTags::new);
        pack.addProvider(BiomeTags::new);
        pack.addProvider(Registries::new);
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

    public static class ItemTags extends FabricTagProvider.ItemTagProvider {
        public ItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
            super(output, completableFuture, blockTagProvider);
        }

        public ItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(net.minecraft.registry.tag.ItemTags.ARROWS)
                    .add(Beryllium.COMBAT.DIAMOND_ARROW)
                    .add(Beryllium.COMBAT.IRON_ARROW)
                    .add(Beryllium.COMBAT.FLAMING_ARROW)
                    .add(Beryllium.COMBAT.COPPER_ARROW);

            getOrCreateTagBuilder(net.minecraft.registry.tag.ItemTags.FLOWERS)
                    .add(Beryllium.EXPLORING.DAFFODILS.asItem())
                    .add(Beryllium.EXPLORING.SCILLA.asItem())
                    .add(Beryllium.EXPLORING.GERANIUMS.asItem());
        }
    }

    public static class Models extends FabricModelProvider {
        public Models(FabricDataOutput output) {
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
        }

        @Override
        public void generateItemModels(ItemModelGenerator gen) {
            gen.register(Beryllium.COMBAT.DIAMOND_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Beryllium.COMBAT.IRON_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Beryllium.COMBAT.FLAMING_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Beryllium.COMBAT.COPPER_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Beryllium.BUILDING.MAGIC_WAND_ITEM, net.minecraft.data.client.Models.GENERATED);
            gen.register(Beryllium.EXPLORING.FIREFLY_BOTTLE.asItem(), net.minecraft.data.client.Models.GENERATED);
            gen.register(Beryllium.FOOD.CROISSANT, net.minecraft.data.client.Models.GENERATED);

            /*
            int directions = 32;

            for (int i = 0; i < directions; i++) {
                String si = i < 10 ? "0" + i : String.valueOf(i);
                float f = (float) i / directions;

                JsonObject needleModel = new JsonObject();
                needleModel.addProperty("parent", "minecraft:item/generated");

                JsonArray overrides = new JsonArray();

                for (DyeColor color : DyeColor.values()) {
                    JsonObject predicate = new JsonObject();
                    JsonObject angle = new JsonObject();
                    angle.addProperty("angle", f);
                    predicate.add("predicate", angle);
                    predicate.addProperty("model", "beryllium:item/compass/" + color + "/" + si);
                    overrides.add(predicate);
                }

                needleModel.add("overrides", overrides);

                gen.writer.accept(Identifier.ofVanilla("item/compass/" + si), () -> needleModel);
            }
             */
        }
    }

    public static class Recipes extends FabricRecipeProvider {
        public Recipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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

            offerSmelting(exporter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.2f, 100, "leather");
            offerMultipleOptions(exporter, RecipeSerializer.SMOKING, SmokingRecipe::new, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.2f, 100, "leather", "_from_smoking");

            /*
            ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Beryllium.BUILDING.MOSSY_STONE.getBaseBlock())
                    .input(Blocks.STONE)
                    .input(Blocks.VINE)
                    .group("mossy_stone")
                    .criterion("has_vine", conditionsFromItem(Blocks.VINE))
                    .offerTo(exporter, convertBetween(Beryllium.BUILDING.MOSSY_STONE.getBaseBlock(), Blocks.VINE));

            generateFamily(exporter, Beryllium.BUILDING.MOSSY_STONE, FeatureSet.empty());
            generateFamily(exporter, Beryllium.BUILDING.CRACKED_STONE_BRICKS, FeatureSet.empty());

            BlockFamilies.STONE.getVariants().forEach((variant, stone) -> {
                Block cobble = BlockFamilies.COBBLESTONE.getVariant(variant);

                if (cobble != null) {
                    firingStone(exporter, List.of(cobble), stone, "stone");
                }
            });
            Beryllium.BUILDING.SMOOTH_STONE.getVariants().forEach((variant, smooth) -> {
                Block stone = BlockFamilies.STONE.getVariant(variant);

                if (stone != null) {
                    firingStone(exporter, List.of(stone), smooth, "smooth_stone");
                }
            });
             */
        }
    }

    public static class BlockLootTables extends FabricBlockLootTableProvider {
        public BlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
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

            addDrop(Beryllium.EXPLORING.DAFFODILS);
            addDrop(Beryllium.EXPLORING.SCILLA);
            addDrop(Beryllium.EXPLORING.GERANIUMS);
        }
    }

    public static class BlockTags extends FabricTagProvider.BlockTagProvider {
        public BlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.PICKAXE_MINEABLE)
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

                    .add(Beryllium.BUILDING.KILN_BLOCK);

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.SHOVEL_MINEABLE)
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getBaseBlock())
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED))
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL))
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.WALLS)
                    .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.WALL))
                    .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL))
                    .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL))
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL));

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.STAIRS)
                    .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS))
                    .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS))
                    .add(Beryllium.BUILDING.SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS))
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS));

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.SLABS)
                    .add(Beryllium.BUILDING.MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB))
                    .add(Beryllium.BUILDING.CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB))
                    .add(Beryllium.BUILDING.SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB));

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                    .add(Beryllium.EXPLORING.DAFFODILS)
                    .add(Beryllium.EXPLORING.SCILLA)
                    .add(Beryllium.EXPLORING.GERANIUMS);

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.SWORD_EFFICIENT)
                    .add(Beryllium.EXPLORING.DAFFODILS)
                    .add(Beryllium.EXPLORING.SCILLA)
                    .add(Beryllium.EXPLORING.GERANIUMS);

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.HOE_MINEABLE)
                    .add(Beryllium.EXPLORING.DAFFODILS)
                    .add(Beryllium.EXPLORING.SCILLA)
                    .add(Beryllium.EXPLORING.GERANIUMS);

            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.FLOWERS)
                    .add(Beryllium.EXPLORING.DAFFODILS)
                    .add(Beryllium.EXPLORING.SCILLA)
                    .add(Beryllium.EXPLORING.GERANIUMS);
        }
    }

    public static class StructureTags extends FabricTagProvider<Structure> {
        public StructureTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.STRUCTURE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(TagKey.of(registryRef, Beryllium.EXPLORING.id("on_bastion_maps")))
                    .add(StructureKeys.BASTION_REMNANT);
            getOrCreateTagBuilder(TagKey.of(registryRef, Beryllium.EXPLORING.id("on_fortress_maps")))
                    .add(StructureKeys.FORTRESS);
            getOrCreateTagBuilder(TagKey.of(registryRef, Beryllium.EXPLORING.id("spawn")))
                    .add(StructureKeys.VILLAGE_PLAINS)
                    .add(StructureKeys.VILLAGE_DESERT)
                    .add(StructureKeys.VILLAGE_SAVANNA)
                    .add(StructureKeys.VILLAGE_SNOWY)
                    .add(StructureKeys.VILLAGE_TAIGA);
        }
    }

    public static class BiomeTags extends FabricTagProvider<Biome> {
        public BiomeTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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

    public static class Registries extends FabricDynamicRegistryProvider {
        public Registries(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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
}
