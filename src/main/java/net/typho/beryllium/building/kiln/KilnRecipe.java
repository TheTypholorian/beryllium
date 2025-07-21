package net.typho.beryllium.building.kiln;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.typho.beryllium.Beryllium;

public class KilnRecipe extends AbstractCookingRecipe {
    public KilnRecipe(String group, CookingRecipeCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(Beryllium.BUILDING.KILN_RECIPE_TYPE, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Beryllium.BUILDING.KILN_BLOCK);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Beryllium.BUILDING.KILN_RECIPE_SERIALIZER;
    }
}
