package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.typho.beryllium.combat.Combat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "getMovementSpeed()F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void bonusSpeed(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() + Combat.bonusSpeed((PlayerEntity) (Object) this));
    }

    @ModifyVariable(
            method = "attack",
            at = @At(value = "STORE", ordinal = 0)
    )
    private float bonusDamage(float f) {
        return f + Combat.bonusMeleeDamage((PlayerEntity) (Object) this);
    }
}
