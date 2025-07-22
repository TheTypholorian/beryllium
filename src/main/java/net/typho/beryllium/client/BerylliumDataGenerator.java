package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.building.kiln.KilnRecipe;
import net.typho.beryllium.exploring.AlgaeBlock;
import net.typho.beryllium.exploring.ExplorationCompassLootFunction;
import net.typho.beryllium.mixin.client.VariantPoolFunctionsAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BerylliumDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        pack.addProvider(ItemTags::new);
        pack.addProvider(Models::new);
        pack.addProvider(Recipes::new);
        pack.addProvider(BlockLootTables::new);
        pack.addProvider(ChestLootTables::new);
        pack.addProvider(BlockTags::new);
        pack.addProvider(StructureTags::new);
        pack.addProvider(BiomeTags::new);
        pack.addProvider(Registries::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder builder) {
        builder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, context -> {
            context.register(Beryllium.EXPLORING.ALGAE_CONFIGURED,
                    new ConfiguredFeature<>(
                            Feature.RANDOM_PATCH,
                            new RandomPatchFeatureConfig(
                                    50, 10, 1, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Beryllium.EXPLORING.ALGAE_BLOCK.getDefaultState().with(Properties.DOWN, true).with(AlgaeBlock.GENERATED, true))))
                            )
                    ));
        });
        builder.addRegistry(RegistryKeys.PLACED_FEATURE, context -> {
            context.register(Beryllium.EXPLORING.ALGAE_PLACED, new PlacedFeature(
                    context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                            .getOrThrow(Beryllium.EXPLORING.ALGAE_CONFIGURED),
                    List.of(CountPlacementModifier.of(4), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of())
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
        }
    }

    public static class Models extends FabricModelProvider {
        public Models(FabricDataOutput output) {
            super(output);
        }

        public static void family(BlockStateModelGenerator gen, BlockFamily family) {
            BlockStateModelGenerator.BlockTexturePool pool = gen.registerCubeAllModelTexturePool(family.getBaseBlock());

            family.getVariants().forEach((variant, block) -> {
                if (variant != null && block != null && block.asItem() != Items.AIR) {
                    VariantPoolFunctionsAccessor.getVariantPools().get(variant).accept(pool, block);
                }
            });
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator gen) {
            family(gen, Beryllium.BUILDING.MOSSY_STONE);
            family(gen, Beryllium.BUILDING.CRACKED_STONE_BRICKS);
            family(gen, Beryllium.BUILDING.SMOOTH_STONE);
            family(gen, Beryllium.BUILDING.SNOW_BRICKS);
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
        }
    }

    public static class ChestLootTables extends SimpleFabricLootTableProvider {
        protected final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;

        public ChestLootTables(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.BARTER);
            this.registryLookup = registryLookup;
        }

        @Override
        public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> out) {
            RegistryWrapper.WrapperLookup lookup = registryLookup.join();

            out.accept(LootTables.JUNGLE_TEMPLE_CHEST, LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(4))
                            .with(ItemEntry.builder(Items.DIAMOND).weight(10)
                                    .apply(SetCountLootFunction.builder(
                                            UniformLootNumberProvider.create(1, 3)
                                    ))
                            )
                            .with(ItemEntry.builder(Items.IRON_INGOT).weight(15)
                                    .apply(SetCountLootFunction.builder(
                                            UniformLootNumberProvider.create(1, 5)
                                    ))
                            )
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(15)
                                    .apply(SetCountLootFunction.builder(
                                            UniformLootNumberProvider.create(2, 7)
                                    ))
                            )
                            .with(ItemEntry.builder(Items.EMERALD).weight(5)
                                    .apply(SetCountLootFunction.builder(
                                            UniformLootNumberProvider.create(1, 3)
                                    ))
                            )
                            .with(ItemEntry.builder(Items.SADDLE).weight(5))
                            .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(5))
                            .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(5))
                            .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(5))
                            .with(ItemEntry.builder(Items.BOOK).weight(5)
                                    .apply(EnchantWithLevelsLootFunction.builder(lookup, UniformLootNumberProvider.create(25, 35)))
                            )
                            .with(ItemEntry.builder(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).weight(20)
                                    .apply(SetCountLootFunction.builder(
                                            UniformLootNumberProvider.create(1, 2)
                                    ))
                            )
                    ));

            out.accept(LootTables.PIGLIN_BARTERING_GAMEPLAY, LootTable.builder()
                    .pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(Items.COMPASS).weight(40)
                            .apply(new ExplorationCompassLootFunction.Builder()
                                    .withDestination(TagKey.of(RegistryKeys.STRUCTURE, Beryllium.EXPLORING.id("on_bastion_maps")))
                                    .searchRadius(100)
                                    .withSkipExistingChunks(false)
                            )
                            .apply(SetNameLootFunction.builder(Text.translatable("item.beryllium.exploring.bastion_compass"), SetNameLootFunction.Target.ITEM_NAME))
                    )
                    .with(ItemEntry.builder(Items.COMPASS).weight(40)
                            .apply(new ExplorationCompassLootFunction.Builder()
                                    .withDestination(TagKey.of(RegistryKeys.STRUCTURE, Beryllium.EXPLORING.id("on_fortress_maps")))
                                    .searchRadius(100)
                                    .withSkipExistingChunks(false)
                            )
                            .apply(SetNameLootFunction.builder(Text.translatable("item.beryllium.exploring.fortress_compass"), SetNameLootFunction.Target.ITEM_NAME))
                    )
                    .with(ItemEntry.builder(Items.BOOK).weight(5)
                            .apply(new EnchantRandomlyLootFunction.Builder()
                                    .option(lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SOUL_SPEED))
                            )
                    )
                    .with(ItemEntry.builder(Items.IRON_BOOTS).weight(8)
                            .apply(EnchantRandomlyLootFunction.builder(lookup)
                                    .option(lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SOUL_SPEED))
                            )
                    )
                    .with(ItemEntry.builder(Items.POTION).weight(8)
                            .apply(SetPotionLootFunction.builder(Potions.FIRE_RESISTANCE))
                    )
                    .with(ItemEntry.builder(Items.SPLASH_POTION).weight(8)
                            .apply(SetPotionLootFunction.builder(Potions.FIRE_RESISTANCE))
                    )
                    .with(ItemEntry.builder(Items.POTION).weight(10)
                            .apply(SetPotionLootFunction.builder(Potions.WATER))
                    )
                    .with(ItemEntry.builder(Items.IRON_NUGGET).weight(10)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(10, 36)
                            ))
                    )
                    .with(ItemEntry.builder(Items.ENDER_PEARL).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(2, 4)
                            ))
                    )
                    .with(ItemEntry.builder(Items.STRING).weight(20)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(3, 9)
                            ))
                    )
                    .with(ItemEntry.builder(Items.QUARTZ).weight(20)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(5, 12)
                            ))
                    )
                    .with(ItemEntry.builder(Items.OBSIDIAN).weight(40))
                    .with(ItemEntry.builder(Items.CRYING_OBSIDIAN).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(1, 3)
                            ))
                    )
                    .with(ItemEntry.builder(Items.FIRE_CHARGE).weight(40))
                    .with(ItemEntry.builder(Items.LEATHER).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(2, 4)
                            ))
                    )
                    .with(ItemEntry.builder(Items.SOUL_SAND).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(2, 8)
                            ))
                    )
                    .with(ItemEntry.builder(Items.NETHER_BRICK).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(2, 8)
                            ))
                    )
                    .with(ItemEntry.builder(Items.SPECTRAL_ARROW).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(6, 12)
                            ))
                    )
                    .with(ItemEntry.builder(Items.GRAVEL).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(8, 16)
                            ))
                    )
                    .with(ItemEntry.builder(Items.BLACKSTONE).weight(40)
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(8, 16)
                            ))
                    )));
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
            getOrCreateTagBuilder(TagKey.of(registryRef, Beryllium.EXPLORING.id("has_fireflies")))
                    .add(BiomeKeys.BIRCH_FOREST)
                    .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
                    .add(BiomeKeys.SWAMP)
                    .add(BiomeKeys.MANGROVE_SWAMP);
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
