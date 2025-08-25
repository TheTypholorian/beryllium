package net.typho.beryllium.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingRecipeJsonBuilder.class)
public interface CraftingRecipeJsonBuilderMixin {
    @ModifyReturnValue(
            method = "getItemId",
            at = @At("RETURN")
    )
    private static Identifier getItemId(Identifier id) {
        if (!id.getNamespace().equals(Beryllium.MOD_ID)) {
            return id;
        }

        return Identifier.of(id.getNamespace(), id.getPath().replace('/', '_'));
    }
}
