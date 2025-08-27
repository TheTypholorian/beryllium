package net.typho.beryllium.mixin.exploring;

import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.render.RenderLayer;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = DefaultMaterials.class, remap = false)
public class DefaultMaterialsMixin {
    @Shadow
    @Final
    public static Material TRANSLUCENT;

    @Inject(
            method = "forRenderLayer",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getMaterial(RenderLayer layer, CallbackInfoReturnable<Material> cir) {
        if (layer == VeilRenderType.get(Beryllium.BASE_CONSTRUCTOR.id("congealed_void"))) {
            cir.setReturnValue(TRANSLUCENT);
        }
    }
}
