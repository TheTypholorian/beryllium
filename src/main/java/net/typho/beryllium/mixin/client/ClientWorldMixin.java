package net.typho.beryllium.mixin.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.typho.beryllium.config.BerylliumConfig;
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
        if (BerylliumConfig.ULTRA_DARK.get()) {
            cir.setReturnValue(1 - BerylliumConfig.ultraDarkBlend((World) (Object) this));
        }
    }
}
