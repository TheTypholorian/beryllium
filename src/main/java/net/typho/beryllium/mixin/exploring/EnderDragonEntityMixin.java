package net.typho.beryllium.mixin.exploring;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {
    @Shadow
    @Nullable
    public EndCrystalEntity connectedCrystal;

    @Inject(
            method = "getFightOrigin",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getFightOrigin(CallbackInfoReturnable<BlockPos> cir) {
        if (connectedCrystal != null) {
            cir.setReturnValue(connectedCrystal.getBlockPos().add(0, 2, 0));
        }
    }

    @Redirect(
            method = "tickWithEndCrystals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Box;expand(D)Lnet/minecraft/util/math/Box;"
            )
    )
    private Box endCrystalDistance(Box instance, double value) {
        return instance.expand(value * 4);
    }
}
