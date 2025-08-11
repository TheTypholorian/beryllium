package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {
    @Shadow
    protected abstract int updateDuration();

    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/effect/StatusEffectInstance;updateDuration()I"
            )
    )
    private void update(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        float cleansing = Armor.bonusCleansing(entity);

        while (cleansing >= 1) {
            cleansing--;
            updateDuration();
        }

        if (entity.getRandom().nextFloat() < cleansing) {
            updateDuration();
        }
    }
}
