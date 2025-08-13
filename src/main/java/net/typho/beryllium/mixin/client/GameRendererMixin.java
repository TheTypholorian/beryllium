package net.typho.beryllium.mixin.client;

import net.minecraft.client.render.GameRenderer;
import net.typho.beryllium.config.Config;
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
        if (Config.ultraDark.get()) {
            cir.setReturnValue(1f);
        }
    }
}
