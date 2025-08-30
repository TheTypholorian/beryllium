package net.typho.beryllium.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.typho.beryllium.config.BerylliumConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "getSkyDarkness",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getSkyDarkness(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (BerylliumConfig.ULTRA_DARK.get()) {
            cir.setReturnValue(BerylliumConfig.ultraDarkBlend(MinecraftClient.getInstance().world));
        }
    }
}
