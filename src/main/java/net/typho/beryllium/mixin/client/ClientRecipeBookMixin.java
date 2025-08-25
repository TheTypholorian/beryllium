package net.typho.beryllium.mixin.client;

import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.RecipeEntry;
import net.typho.beryllium.building.kiln.KilnRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(
            method = "getGroupForRecipe",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getGroupForRecipe(RecipeEntry<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.value() instanceof KilnRecipe) {
            cir.setReturnValue(RecipeBookGroup.BLAST_FURNACE_BLOCKS);
        }
    }
}
