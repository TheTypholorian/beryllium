package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.building.BlockSet;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.building.kiln.KilnBlock;
import net.typho.beryllium.combat.Combat;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BerylliumDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ItemTags::new);
        pack.addProvider(Models::new);
        pack.addProvider(Recipes::new);
        pack.addProvider(BlockLootTables::new);
        pack.addProvider(BlockTags::new);
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
                    .add(Combat.DIAMOND_ARROW)
                    .add(Combat.IRON_ARROW)
                    .add(Combat.FLAMING_ARROW)
                    .add(Combat.COPPER_ARROW);
        }
    }

    public static class Models extends FabricModelProvider {
        public Models(FabricDataOutput output) {
            super(output);
        }

        public static void vanillaWood(BlockStateModelGenerator gen, BlockSet set) {
            gen.registerSimpleCubeAll(set.chiseled);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator gen) {
            vanillaWood(gen, Building.OAK_BLOCK_SET);
            vanillaWood(gen, Building.SPRUCE_BLOCK_SET);
            vanillaWood(gen, Building.BIRCH_BLOCK_SET);
            vanillaWood(gen, Building.JUNGLE_BLOCK_SET);
            vanillaWood(gen, Building.ACACIA_BLOCK_SET);
            vanillaWood(gen, Building.DARK_OAK_BLOCK_SET);
            vanillaWood(gen, Building.MANGROVE_BLOCK_SET);
            vanillaWood(gen, Building.CHERRY_BLOCK_SET);
            vanillaWood(gen, Building.BAMBOO_BLOCK_SET);
            vanillaWood(gen, Building.CRIMSON_BLOCK_SET);
            vanillaWood(gen, Building.WARPED_BLOCK_SET);

            BlockStateModelGenerator.BlockTexturePool stonePool = gen.registerCubeAllModelTexturePool(Building.STONE_BLOCK_SET.solid);
            stonePool.wall(Building.STONE_BLOCK_SET.wall);

            BlockStateModelGenerator.BlockTexturePool mossyStonePool = gen.registerCubeAllModelTexturePool(Building.MOSSY_STONE_BLOCK_SET.solid);
            mossyStonePool.stairs(Building.MOSSY_STONE_BLOCK_SET.stairs);
            mossyStonePool.slab(Building.MOSSY_STONE_BLOCK_SET.slab);
            mossyStonePool.wall(Building.MOSSY_STONE_BLOCK_SET.wall);
            mossyStonePool.pressurePlate(Building.MOSSY_STONE_BLOCK_SET.pressurePlate);

            BlockStateModelGenerator.BlockTexturePool smoothStonePool = gen.registerCubeAllModelTexturePool(Building.SMOOTH_STONE_BLOCK_SET.solid);
            smoothStonePool.stairs(Building.SMOOTH_STONE_BLOCK_SET.stairs);
            // no datagen for this wall due to custom textures
            //smoothStonePool.wall(Building.SMOOTH_STONE_BLOCK_SET.wall);

            BlockStateModelGenerator.BlockTexturePool quartzBricksPool = gen.registerCubeAllModelTexturePool(Building.QUARTZ_BRICKS_BLOCK_SET.solid);
            quartzBricksPool.stairs(Building.QUARTZ_BRICKS_BLOCK_SET.stairs);
            quartzBricksPool.slab(Building.QUARTZ_BRICKS_BLOCK_SET.slab);
            quartzBricksPool.wall(Building.QUARTZ_BRICKS_BLOCK_SET.wall);

            BlockStateModelGenerator.BlockTexturePool smoothQuartzPool = gen.registerCubeAllModelTexturePool(Building.SMOOTH_QUARTZ_BLOCK_SET.solid);
            smoothQuartzPool.wall(Building.SMOOTH_QUARTZ_BLOCK_SET.wall);

            //BlockStateModelGenerator.BlockTexturePool quartzPool = gen.registerCubeAllModelTexturePool(Building.QUARTZ_BLOCK_SET.solid);
            //quartzPool.wall(Building.QUARTZ_BLOCK_SET.wall);
        }

        @Override
        public void generateItemModels(ItemModelGenerator gen) {
            gen.register(Combat.DIAMOND_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Combat.IRON_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Combat.FLAMING_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Combat.COPPER_ARROW, net.minecraft.data.client.Models.GENERATED);
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

        public static void compact(RecipeExporter gen, Block result, Block ingredient) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result, 4)
                    .pattern(" A ")
                    .pattern("A A")
                    .pattern(" A ")
                    .input('A', ingredient)
                    .criterion("has_" + Registries.BLOCK.getId(ingredient).getPath(), FabricRecipeProvider.conditionsFromItem(ingredient))
                    .offerTo(gen, Registries.BLOCK.getId(result));
        }

        public static void wall(RecipeExporter gen, Block result, Block ingredient) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result, 6)
                    .pattern("AAA")
                    .pattern("AAA")
                    .input('A', ingredient)
                    .criterion("has_" + Registries.BLOCK.getId(ingredient).getPath(), FabricRecipeProvider.conditionsFromItem(ingredient))
                    .offerTo(gen, Registries.BLOCK.getId(result));
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
            offerMultipleOptions(exporter, KilnBlock.RECIPE_SERIALIZER, KilnBlock.Recipe::new, inputs, category, output, experience, cookingTime, group, "_from_firing");
        }

        public static void vanillaWood(RecipeExporter gen, BlockSet set) {
            compact(gen, set.chiseled, set.solid);
        }

        public static void firingStone(RecipeExporter gen, List<ItemConvertible> inputs, ItemConvertible output, String group) {
            offerFiring(gen, inputs, RecipeCategory.BUILDING_BLOCKS, output,
                    0.1f, 100, group);
        }

        public static void firingBlockSetsHelper(RecipeExporter gen, ItemConvertible input, ItemConvertible output, String group) {
            if (input != null && output != null) {
                firingStone(gen, List.of(input), output, group);
            }
        }

        public static void firingBlockSets(RecipeExporter gen, BlockSet in, BlockSet out, String group) {
            firingBlockSetsHelper(gen, in.solid, out.solid, group);
            firingBlockSetsHelper(gen, in.chiseled, out.chiseled, group);
            firingBlockSetsHelper(gen, in.pillar, out.pillar, group);
            firingBlockSetsHelper(gen, in.stairs, out.stairs, group);
            firingBlockSetsHelper(gen, in.slab, out.slab, group);
            firingBlockSetsHelper(gen, in.button, out.button, group);
            firingBlockSetsHelper(gen, in.pressurePlate, out.pressurePlate, group);
            firingBlockSetsHelper(gen, in.fence, out.fence, group);
            firingBlockSetsHelper(gen, in.fenceGate, out.fenceGate, group);
            firingBlockSetsHelper(gen, in.sign, out.sign, group);
            firingBlockSetsHelper(gen, in.wallSign, out.wallSign, group);
            firingBlockSetsHelper(gen, in.wall, out.wall, group);
            firingBlockSetsHelper(gen, in.door, out.door, group);
            firingBlockSetsHelper(gen, in.trapdoor, out.trapdoor, group);
        }

        @Override
        public void generate(RecipeExporter exporter) {
            arrow(exporter, Combat.DIAMOND_ARROW, Items.DIAMOND, "has_diamond");
            arrow(exporter, Combat.IRON_ARROW, Items.IRON_INGOT, "has_iron_ingot");
            arrow(exporter, Combat.FLAMING_ARROW, Items.FIRE_CHARGE, "has_fire_charge");
            arrow(exporter, Combat.COPPER_ARROW, Items.COPPER_INGOT, "has_copper_ingot");

            offerNetheriteUpgradeRecipe(exporter, Combat.DIAMOND_ARROW, RecipeCategory.COMBAT, Combat.NETHERITE_GLAIVE);
            glaive(exporter, Combat.DIAMOND_GLAIVE, Items.DIAMOND, "has_diamond");
            glaive(exporter, Combat.IRON_GLAIVE, Items.IRON_INGOT, "has_iron_ingot");
            glaive(exporter, Combat.GOLDEN_GLAIVE, Items.GOLD_INGOT, "has_gold_ingot");

            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Building.KILN_BLOCK, 1)
                    .pattern("AAA")
                    .pattern("A A")
                    .pattern("AAA")
                    .input('A', Items.BRICK)
                    .criterion("has_brick", FabricRecipeProvider.conditionsFromItem(Items.BRICK))
                    .offerTo(exporter, Identifier.of(Beryllium.MOD_ID, "kiln"));

            vanillaWood(exporter, Building.OAK_BLOCK_SET);
            vanillaWood(exporter, Building.SPRUCE_BLOCK_SET);
            vanillaWood(exporter, Building.BIRCH_BLOCK_SET);
            vanillaWood(exporter, Building.JUNGLE_BLOCK_SET);
            vanillaWood(exporter, Building.ACACIA_BLOCK_SET);
            vanillaWood(exporter, Building.DARK_OAK_BLOCK_SET);
            vanillaWood(exporter, Building.MANGROVE_BLOCK_SET);
            vanillaWood(exporter, Building.CHERRY_BLOCK_SET);

            wall(exporter, Building.STONE_BLOCK_SET.wall, Building.STONE_BLOCK_SET.solid);
            firingBlockSets(exporter, Building.COBBLESTONE_BLOCK_SET, Building.STONE_BLOCK_SET, "stone");
            firingBlockSets(exporter, Building.STONE_BLOCK_SET, Building.SMOOTH_STONE_BLOCK_SET, "smooth_stone");
            firingBlockSets(exporter, Building.QUARTZ_BLOCK_SET, Building.SMOOTH_QUARTZ_BLOCK_SET, "smooth_quartz");
        }
    }

    public static class BlockLootTables extends FabricBlockLootTableProvider {
        public BlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        public void vanillaWood(BlockSet set) {
            addDrop(set.chiseled);
        }

        @Override
        public void generate() {
            vanillaWood(Building.OAK_BLOCK_SET);
            vanillaWood(Building.SPRUCE_BLOCK_SET);
            vanillaWood(Building.BIRCH_BLOCK_SET);
            vanillaWood(Building.JUNGLE_BLOCK_SET);
            vanillaWood(Building.ACACIA_BLOCK_SET);
            vanillaWood(Building.DARK_OAK_BLOCK_SET);
            vanillaWood(Building.MANGROVE_BLOCK_SET);
            vanillaWood(Building.CHERRY_BLOCK_SET);
            addDrop(Building.STONE_BLOCK_SET.wall);
            addDrop(Building.MOSSY_STONE_BLOCK_SET.solid);
            addDrop(Building.MOSSY_STONE_BLOCK_SET.stairs);
            addDrop(Building.MOSSY_STONE_BLOCK_SET.slab);
            addDrop(Building.MOSSY_STONE_BLOCK_SET.wall);
            addDrop(Building.MOSSY_STONE_BLOCK_SET.pressurePlate);
            addDrop(Building.SMOOTH_QUARTZ_BLOCK_SET.stairs);
            addDrop(Building.SMOOTH_QUARTZ_BLOCK_SET.wall);
            addDrop(Building.QUARTZ_BLOCK_SET.wall);
            addDrop(Building.QUARTZ_BRICKS_BLOCK_SET.stairs);
            addDrop(Building.QUARTZ_BRICKS_BLOCK_SET.slab);
            addDrop(Building.QUARTZ_BRICKS_BLOCK_SET.wall);
            addDrop(Building.SMOOTH_QUARTZ_BLOCK_SET.wall);
            addDrop(Building.KILN_BLOCK);
        }
    }

    public static class BlockTags extends FabricTagProvider.BlockTagProvider {
        public BlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.PICKAXE_MINEABLE)
                    .add(Building.STONE_BLOCK_SET.wall)
                    .add(Building.MOSSY_STONE_BLOCK_SET.solid)
                    .add(Building.MOSSY_STONE_BLOCK_SET.stairs)
                    .add(Building.MOSSY_STONE_BLOCK_SET.slab)
                    .add(Building.MOSSY_STONE_BLOCK_SET.wall)
                    .add(Building.MOSSY_STONE_BLOCK_SET.pressurePlate)
                    .add(Building.SMOOTH_STONE_BLOCK_SET.stairs)
                    .add(Building.SMOOTH_STONE_BLOCK_SET.wall)
                    .add(Building.QUARTZ_BLOCK_SET.wall)
                    .add(Building.QUARTZ_BRICKS_BLOCK_SET.stairs)
                    .add(Building.QUARTZ_BRICKS_BLOCK_SET.slab)
                    .add(Building.QUARTZ_BRICKS_BLOCK_SET.wall)
                    .add(Building.SMOOTH_QUARTZ_BLOCK_SET.wall)
                    .add(Building.KILN_BLOCK);
            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.WALLS)
                    .add(Building.STONE_BLOCK_SET.wall)
                    .add(Building.MOSSY_STONE_BLOCK_SET.wall)
                    .add(Building.SMOOTH_STONE_BLOCK_SET.wall)
                    .add(Building.QUARTZ_BLOCK_SET.wall)
                    .add(Building.QUARTZ_BRICKS_BLOCK_SET.wall)
                    .add(Building.SMOOTH_QUARTZ_BLOCK_SET.wall);
            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.STAIRS)
                    .add(Building.MOSSY_STONE_BLOCK_SET.stairs)
                    .add(Building.SMOOTH_STONE_BLOCK_SET.stairs)
                    .add(Building.QUARTZ_BRICKS_BLOCK_SET.stairs);
            getOrCreateTagBuilder(net.minecraft.registry.tag.BlockTags.SLABS)
                    .add(Building.MOSSY_STONE_BLOCK_SET.slab)
                    .add(Building.QUARTZ_BRICKS_BLOCK_SET.slab);
        }
    }
}
