package net.typho.beryllium.mixin;

import net.neoforged.neoforge.registries.GameData;
import net.typho.beryllium.RemovedThings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameData.class)
public class GameDataMixin {
    @Inject(
            method = "postRegisterEvents",
            at = @At("TAIL")
    )
    private static void postRegisterEvents(CallbackInfo ci) {
        RemovedThings.reload();
    }
}
