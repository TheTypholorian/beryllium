package net.typho.beryllium.mixin.client;

import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeProvider.class)
public class RecipeProviderMixin {
    @Inject(
            method = "getItemPath",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getItemId(ItemConvertible item, CallbackInfoReturnable<String> cir) {
        Identifier id = Registries.ITEM.getId(item.asItem());

        if (id.getNamespace().equals(Beryllium.MOD_ID)) {
            cir.setReturnValue(id.getPath().replace('/', '_'));
        }
    }
}
