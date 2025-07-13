package net.typho.beryllium.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.typho.beryllium.combat.Combat;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BerylliumDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ItemTags::new);
        pack.addProvider(Models::new);
        pack.addProvider(Recipes::new);
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

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator gen) {
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
        }
    }
}
