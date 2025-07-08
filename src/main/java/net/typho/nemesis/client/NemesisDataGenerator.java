package net.typho.nemesis.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.typho.nemesis.Nemesis;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NemesisDataGenerator implements DataGeneratorEntrypoint {
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
                    .add(Nemesis.DIAMOND_ARROW)
                    .add(Nemesis.IRON_ARROW)
                    .add(Nemesis.FLAMING_ARROW)
                    .add(Nemesis.SHOCK_ARROW);
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
            gen.register(Nemesis.DIAMOND_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Nemesis.IRON_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Nemesis.FLAMING_ARROW, net.minecraft.data.client.Models.GENERATED);
            gen.register(Nemesis.SHOCK_ARROW, net.minecraft.data.client.Models.GENERATED);
        }
    }

    public static class Recipes extends FabricRecipeProvider {
        public Recipes(FabricDataOutput output) {
            super(output);
        }

        public static void arrow(Consumer<RecipeJsonProvider> consumer, Item result, Item tip, String tipCriterion) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result, 4)
                    .input('#', Items.STICK)
                    .input('X', tip)
                    .input('Y', Items.FEATHER)
                    .pattern("X")
                    .pattern("#")
                    .pattern("Y")
                    .criterion("has_feather", conditionsFromItem(Items.FEATHER))
                    .criterion(tipCriterion, conditionsFromItem(tip))
                    .offerTo(consumer);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> consumer) {
            arrow(consumer, Nemesis.DIAMOND_ARROW, Items.DIAMOND, "has_diamond");
            arrow(consumer, Nemesis.IRON_ARROW, Items.IRON_INGOT, "has_iron_ingot");
            arrow(consumer, Nemesis.FLAMING_ARROW, Items.FIRE_CHARGE, "has_fire_charge");
            arrow(consumer, Nemesis.SHOCK_ARROW, Items.COPPER_INGOT, "has_copper_ingot");
        }
    }
}
