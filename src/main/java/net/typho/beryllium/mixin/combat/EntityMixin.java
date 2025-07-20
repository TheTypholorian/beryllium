package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "isBeingRainedOn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isBeingRainedOn(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof LivingEntity living) {
            if (living.hasStatusEffect(Beryllium.COMBAT.WET_EFFECT)) {
                cir.setReturnValue(true);
            }
        }
    }
}
