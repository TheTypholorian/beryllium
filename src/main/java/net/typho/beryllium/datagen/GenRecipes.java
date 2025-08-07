package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.building.kiln.KilnRecipe;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.food.Food;
import net.typho.beryllium.redstone.Redstone;
import net.typho.beryllium.util.BlockFamilyBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GenRecipes extends FabricRecipeProvider {
    public GenRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public static void firingVanillaRecipes(RecipeExporter exporter) {
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Items.CLAY_BALL), RecipeCategory.MISC, Items.BRICK, 0.3f, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_clay_ball", conditionsFromItem(Items.CLAY_BALL))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.CLAY), RecipeCategory.BUILDING_BLOCKS, Blocks.TERRACOTTA.asItem(), 0.35f, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_clay_block", conditionsFromItem(Blocks.CLAY))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.fromTag(ItemTags.LOGS_THAT_BURN), RecipeCategory.MISC, Items.CHARCOAL, 0.15f, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_log", conditionsFromTag(ItemTags.LOGS_THAT_BURN))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.NETHERRACK), RecipeCategory.MISC, Items.NETHER_BRICK, 0.1f, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_netherrack", conditionsFromItem(Blocks.NETHERRACK))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.fromTag(ItemTags.SMELTS_TO_GLASS), RecipeCategory.BUILDING_BLOCKS, Blocks.GLASS, 0.1f, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_smelts_to_glass", conditionsFromTag(ItemTags.SMELTS_TO_GLASS))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.SANDSTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_SANDSTONE.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_sandstone", conditionsFromItem(Blocks.SANDSTONE))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.RED_SANDSTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_RED_SANDSTONE.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_red_sandstone", conditionsFromItem(Blocks.RED_SANDSTONE))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.QUARTZ_BLOCK), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_QUARTZ.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_quartz_block", conditionsFromItem(Blocks.QUARTZ_BLOCK))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.BLACK_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BLACK_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_black_terracotta", conditionsFromItem(Blocks.BLACK_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.BLUE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_blue_terracotta", conditionsFromItem(Blocks.BLUE_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.BROWN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BROWN_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_brown_terracotta", conditionsFromItem(Blocks.BROWN_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.CYAN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.CYAN_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_cyan_terracotta", conditionsFromItem(Blocks.CYAN_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.GRAY_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_gray_terracotta", conditionsFromItem(Blocks.GRAY_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.GREEN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.GREEN_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_green_terracotta", conditionsFromItem(Blocks.GREEN_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.LIGHT_BLUE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_light_blue_terracotta", conditionsFromItem(Blocks.LIGHT_BLUE_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.LIGHT_GRAY_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_light_gray_terracotta", conditionsFromItem(Blocks.LIGHT_GRAY_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.LIME_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIME_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_lime_terracotta", conditionsFromItem(Blocks.LIME_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.MAGENTA_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.MAGENTA_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_magenta_terracotta", conditionsFromItem(Blocks.MAGENTA_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.ORANGE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.ORANGE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_orange_terracotta", conditionsFromItem(Blocks.ORANGE_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.PINK_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.PINK_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_pink_terracotta", conditionsFromItem(Blocks.PINK_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.PURPLE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.PURPLE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_purple_terracotta", conditionsFromItem(Blocks.PURPLE_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.RED_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.RED_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_red_terracotta", conditionsFromItem(Blocks.RED_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.WHITE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.WHITE_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_white_terracotta", conditionsFromItem(Blocks.WHITE_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(
                        Ingredient.ofItems(Blocks.YELLOW_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.YELLOW_GLAZED_TERRACOTTA.asItem(), 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new
                )
                .criterion("has_yellow_terracotta", conditionsFromItem(Blocks.YELLOW_TERRACOTTA))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.BASALT), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_BASALT, 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_basalt", conditionsFromItem(Blocks.BASALT))
                .offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(Blocks.COBBLED_DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE, 0.1F, 100, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new)
                .criterion("has_cobbled_deepslate", conditionsFromItem(Blocks.COBBLED_DEEPSLATE))
                .offerTo(exporter);
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
        offerMultipleOptions(exporter, Building.KILN_RECIPE_SERIALIZER, KilnRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_firing");
    }

    public static void firingStone(RecipeExporter gen, List<ItemConvertible> inputs, ItemConvertible output, String group) {
        offerFiring(gen, inputs, RecipeCategory.BUILDING_BLOCKS, output, 0.1f, 100, group);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Building.KILN_BLOCK.asItem(), 1)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .input('A', Items.BRICK)
                .criterion("has_brick", FabricRecipeProvider.conditionsFromItem(Items.BRICK))
                .offerTo(exporter, Building.CONSTRUCTOR.id("kiln"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, Exploring.METAL_DETECTOR_ITEM, 1)
                .pattern(" C ")
                .pattern("CRC")
                .pattern(" C ")
                .input('C', Items.COPPER_INGOT)
                .input('R', Items.REDSTONE)
                .criterion("has_redstone", FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("metal_detector"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.LODESTONE, 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .input('A', Items.CHISELED_STONE_BRICKS)
                .input('B', Items.IRON_INGOT)
                .criterion("has_chiseled_stone_bricks", FabricRecipeProvider.conditionsFromItem(Items.CHISELED_STONE_BRICKS))
                .offerTo(exporter, Identifier.ofVanilla("lodestone"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, Food.CROISSANT, 1)
                .pattern(" C ")
                .pattern("WWW")
                .input('C', Items.COCOA_BEANS)
                .input('W', Items.WHEAT)
                .criterion("has_cocoa_beans", FabricRecipeProvider.conditionsFromItem(Items.COCOA_BEANS))
                .offerTo(exporter, Food.CONSTRUCTOR.id("croissant"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, Food.BAGUETTE, 1)
                .pattern("WWW")
                .pattern("WWW")
                .input('W', Items.WHEAT)
                .criterion("has_wheat", FabricRecipeProvider.conditionsFromItem(Items.WHEAT))
                .offerTo(exporter, Food.CONSTRUCTOR.id("baguette"));

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

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Redstone.GOLD_HOPPER_BLOCK, 1)
                .pattern("GRG")
                .pattern("GCG")
                .pattern(" G ")
                .input('G', Items.GOLD_INGOT)
                .input('R', Items.REDSTONE)
                .input('C', Items.CHEST)
                .criterion("has_gold_ingot", FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter, Redstone.CONSTRUCTOR.id("gold_hopper"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Redstone.DESTRUCTOR_BLOCK, 1)
                .pattern("CCC")
                .pattern("CPC")
                .pattern("CRC")
                .input('C', Items.COBBLESTONE)
                .input('R', Items.REDSTONE)
                .input('P', Items.IRON_PICKAXE)
                .criterion("has_iron_pickaxe", FabricRecipeProvider.conditionsFromItem(Items.IRON_PICKAXE))
                .offerTo(exporter, Redstone.CONSTRUCTOR.id("destructor_block"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Items.SLIME_BALL, 1)
                .input(Items.MAGMA_CREAM)
                .input(Items.HONEY_BOTTLE)
                .input(Items.LIME_DYE)
                .criterion("has_honey_bottle", FabricRecipeProvider.conditionsFromItem(Items.HONEY_BOTTLE))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("slimeball"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.STRING, 4)
                .input(ItemTags.WOOL)
                .input(Items.SHEARS)
                .criterion("has_wool", FabricRecipeProvider.conditionsFromTag(ItemTags.WOOL))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("string_from_wool"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.STRING, 2)
                .input(ItemTags.WOOL_CARPETS)
                .input(Items.SHEARS)
                .criterion("has_carpet", FabricRecipeProvider.conditionsFromTag(ItemTags.WOOL_CARPETS))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("string_from_carpet"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.GRAVEL, 1)
                .input(Items.WATER_BUCKET)
                .input(Items.COBBLESTONE)
                .criterion("has_water", FabricRecipeProvider.conditionsFromItem(Items.WATER_BUCKET))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("gravel_from_washing"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.SAND, 1)
                .input(Items.WATER_BUCKET)
                .input(Items.GRAVEL)
                .criterion("has_water", FabricRecipeProvider.conditionsFromItem(Items.WATER_BUCKET))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("sand_from_washing"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.IRON_NUGGET, 1)
                .input(Items.WATER_BUCKET)
                .input(Items.SAND)
                .criterion("has_water", FabricRecipeProvider.conditionsFromItem(Items.WATER_BUCKET))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("iron_nugget_from_washing"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Exploring.BLAZING_TORCH_ITEM, 1)
                .pattern("C")
                .pattern("B")
                .input('C', ItemTags.COALS)
                .input('B', Items.BLAZE_ROD)
                .criterion("has_coals", FabricRecipeProvider.conditionsFromTag(ItemTags.COALS))
                .offerTo(exporter, Exploring.CONSTRUCTOR.id("blazing_torch"));

        arrow(exporter, Combat.DIAMOND_ARROW, Items.DIAMOND, "has_diamond");
        arrow(exporter, Combat.IRON_ARROW, Items.IRON_INGOT, "has_iron_ingot");
        arrow(exporter, Combat.FLAMING_ARROW, Items.FIRE_CHARGE, "has_fire_charge");
        arrow(exporter, Combat.COPPER_ARROW, Items.COPPER_INGOT, "has_copper_ingot");

        offerNetheriteUpgradeRecipe(exporter, Combat.DIAMOND_GLAIVE, RecipeCategory.COMBAT, Combat.NETHERITE_GLAIVE);
        glaive(exporter, Combat.DIAMOND_GLAIVE, Items.DIAMOND, "has_diamond");
        glaive(exporter, Combat.IRON_GLAIVE, Items.IRON_INGOT, "has_iron_ingot");
        glaive(exporter, Combat.GOLDEN_GLAIVE, Items.GOLD_INGOT, "has_gold_ingot");

        offerNetheriteUpgradeRecipe(exporter, Combat.DIAMOND_SCYTHE, RecipeCategory.COMBAT, Combat.NETHERITE_SCYTHE);
        scythe(exporter, Combat.DIAMOND_SCYTHE, Items.DIAMOND, "has_diamond");
        scythe(exporter, Combat.IRON_SCYTHE, Items.IRON_INGOT, "has_iron_ingot");
        scythe(exporter, Combat.GOLDEN_SCYTHE, Items.GOLD_INGOT, "has_gold_ingot");

        offerNetheriteUpgradeRecipe(exporter, Items.DIAMOND_HORSE_ARMOR, RecipeCategory.COMBAT, Combat.NETHERITE_HORSE_ARMOR);

        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, Blocks.BONE_BLOCK, Exploring.POINTED_BONE);

        offerSmelting(exporter, List.of(Items.SUGAR_CANE), RecipeCategory.MISC, Items.LIME_DYE, 1, 200, "lime_dye");

        offerSmelting(exporter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.2f, 100, "leather");
        offerMultipleOptions(exporter, RecipeSerializer.SMOKING, SmokingRecipe::new, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.2f, 100, "leather", "_from_smoking");

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Building.MOSSY_STONE.getBaseBlock())
                .input(Blocks.STONE)
                .input(Blocks.VINE)
                .group("mossy_stone")
                .criterion("has_vine", conditionsFromItem(Blocks.VINE))
                .offerTo(exporter, convertBetween(Building.MOSSY_STONE.getBaseBlock(), Blocks.STONE));
        BlockFamilies.STONE.getVariants().forEach((variant, stone) -> {
            Block mossy = Building.MOSSY_STONE.getVariant(variant);

            if (mossy != null) {
                ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, mossy)
                        .input(stone)
                        .input(Blocks.VINE)
                        .group("mossy_stone")
                        .criterion("has_vine", conditionsFromItem(Blocks.VINE))
                        .offerTo(exporter, convertBetween(mossy, stone));
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
        Building.SMOOTH_STONE.getVariants().forEach((variant, smooth) -> {
            Block stone = BlockFamilies.STONE.getVariant(variant);

            if (stone != null) {
                firingStone(exporter, List.of(stone), smooth, "smooth_stone");
            }
        });
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, Building.SNOW_BRICKS.getBaseBlock(), Blocks.SNOW_BLOCK);

        firingVanillaRecipes(exporter);

        generateFamily(exporter, Building.MOSSY_STONE, FeatureSet.empty());
        generateFamily(exporter, Building.CRACKED_STONE_BRICKS, FeatureSet.empty());
        generateFamily(exporter, Building.SMOOTH_STONE, FeatureSet.empty());
        generateFamily(exporter, Building.SNOW_BRICKS, FeatureSet.empty());

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            generateFamily(exporter, family.build(), FeatureSet.empty());
        }
    }
}
