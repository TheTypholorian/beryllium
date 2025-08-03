package net.typho.beryllium.mixin.client;

import net.minecraft.client.world.ClientWorld;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(
            method = "getSkyBrightness",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getSkyBrightness(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (Beryllium.SERVER_CONFIG.ultraDark.get()) {
            cir.setReturnValue(0f);
        }
    }
}
