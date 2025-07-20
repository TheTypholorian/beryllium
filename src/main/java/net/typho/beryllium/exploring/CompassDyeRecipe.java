package net.typho.beryllium.exploring;

import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;

public class CompassDyeRecipe implements CraftingRecipe {
    public static final RecipeSerializer<CompassDyeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(CompassDyeRecipe::new);

    public final CraftingRecipeCategory category;

    public CompassDyeRecipe(CraftingRecipeCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getSize() != 2) {
            return false;
        }

        ItemStack a = input.getStacks().getFirst(), b = input.getStacks().getLast();

        return (a.getItem() == Items.COMPASS && b.getItem() instanceof DyeItem) || (b.getItem() == Items.COMPASS && a.getItem() instanceof DyeItem);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack baseCompass = input.getStacks().stream()
                .filter(i -> i.getItem() == Items.COMPASS)
                .findFirst()
                .map(ItemStack::copy)
                .orElseThrow();

        input.getStacks().stream()
                .map(ItemStack::getItem)
                .filter(i -> i instanceof DyeItem)
                .map(i -> ((DyeItem) i).getColor())
                .findFirst()
                .ifPresent(color -> baseCompass.set(Beryllium.EXPLORING.COMPASS_NEEDLE_COMPONENT, color));

        return baseCompass;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return new ItemStack(Items.COMPASS);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return category;
    }
}