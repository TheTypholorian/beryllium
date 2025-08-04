package net.typho.beryllium.mixin;

import net.minecraft.world.dimension.DimensionType;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(
            method = "monsterSpawnBlockLightLimit",
            at = @At("RETURN"),
            cancellable = true
    )
    private void monsterSpawnBlockLightLimit(CallbackInfoReturnable<Integer> cir) {
        if (Beryllium.SERVER_CONFIG.ultraDark.get()) {
            cir.setReturnValue(Math.max(cir.getReturnValue(), 7));
        }
    }
}
