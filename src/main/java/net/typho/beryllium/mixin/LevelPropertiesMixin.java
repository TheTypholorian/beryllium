package net.typho.beryllium.mixin;

import net.minecraft.world.level.LevelProperties;
import net.typho.beryllium.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {
    @Inject(
            method = "getTimeOfDay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getTimeOfDay(CallbackInfoReturnable<Long> cir) {
        if (Config.ultraDark.get()) {
            cir.setReturnValue(18000L);
        }
    }

    @Inject(
            method = "isThundering",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isThundering(CallbackInfoReturnable<Boolean> cir) {
        if (Config.ultraDark.get()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "isRaining",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isRaining(CallbackInfoReturnable<Boolean> cir) {
        if (Config.ultraDark.get()) {
            cir.setReturnValue(true);
        }
    }
}
