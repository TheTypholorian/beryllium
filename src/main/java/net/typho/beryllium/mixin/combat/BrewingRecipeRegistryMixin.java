package net.typho.beryllium.mixin.combat;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Shadow
    @Final
    private List<BrewingRecipeRegistry.Recipe<Item>> itemRecipes;

    @Shadow
    @Final
    private List<BrewingRecipeRegistry.Recipe<Potion>> potionRecipes;

    /**
     * @author The Typhothanian
     * @reason Copy custom per-stack effects (needed for splash cocktails)
     */
    @Overwrite
    @SuppressWarnings("deprecation")
    public ItemStack craft(ItemStack ingredient, ItemStack input) {
        if (!input.isEmpty()) {
            PotionContentsComponent contents = input.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);

            if (contents.hasEffects()) {
                for (net.minecraft.recipe.BrewingRecipeRegistry.Recipe<Item> recipe : itemRecipes) {
                    if (input.itemMatches(recipe.from()) && recipe.ingredient().test(ingredient)) {
                        ItemStack stack = new ItemStack(recipe.to().value());
                        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(contents.potion(), contents.customColor(), contents.customEffects()));
                        return stack;
                    }
                }

                Optional<RegistryEntry<Potion>> optional = contents.potion();

                if (optional.isPresent()) {
                    for (net.minecraft.recipe.BrewingRecipeRegistry.Recipe<Potion> recipe : potionRecipes) {
                        if (recipe.from().matches(optional.get()) && recipe.ingredient().test(ingredient)) {
                            return PotionContentsComponent.createStack(input.getItem(), recipe.to());
                        }
                    }
                }
            }
        }

        return input;
    }
}
