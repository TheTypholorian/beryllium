package net.typho.beryllium.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.typho.beryllium.config.BerylliumConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(
            method = {
                    "acceptsMessage",
                    "acceptsChatMessage"
            },
            at = @At("HEAD"),
            cancellable = true
    )
    private void acceptsMessage(CallbackInfoReturnable<Boolean> cir) {
        if (BerylliumConfig.DISABLED_CHAT.get()) {
            cir.setReturnValue(false);
        }
    }
}
