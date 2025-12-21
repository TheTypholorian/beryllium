package net.typho.beryllium.mixin.tooltips;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.typho.beryllium.tooltips.ItemIsMaterialStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    private Map<ResourceLocation, RecipeHolder<?>> byName;

    @Inject(
            method = {
                    "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
                    "replaceRecipes"
            },
            at = @At("TAIL")
    )
    private void markIsMaterials(CallbackInfo ci) {
        for (Item item : BuiltInRegistries.ITEM) {
            ItemIsMaterialStorage.set(item, false);
        }

        for (RecipeHolder<?> recipe : byName.values()) {
            for (Ingredient ingredient : recipe.value().getIngredients()) {
                for (ItemStack item : ingredient.getItems()) {
                    ItemIsMaterialStorage.set(item.getItem(), true);
                }
            }
        }
    }
}
