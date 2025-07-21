package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.typho.beryllium.combat.ScytheItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    @NotNull
    public abstract ItemStack getWeaponStack();

    @Redirect(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"
            )
    )
    private void knockback(LivingEntity living, double strength, double x, double z) {
        if (living.getLastAttacker() != null) {
            living.takeKnockback(living.getLastAttacker().getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK) * strength, x, z);
        } else {
            living.takeKnockback(strength, x, z);
        }
    }

    @Inject(
            method = "takeKnockback",
            at = @At("HEAD"),
            cancellable = true
    )
    private void takeKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        strength *= 1 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);

        entity.velocityDirty = true;
        Vec3d vec3d = entity.getVelocity();

        while (x * x + z * z < 1.0E-5F) {
            x = (Math.random() - Math.random()) * 0.01;
            z = (Math.random() - Math.random()) * 0.01;
        }

        Vec3d vec3d2 = new Vec3d(x, 0, z).normalize().multiply(strength);
        entity.setVelocity(vec3d.x / 2.0 - vec3d2.x, entity.isOnGround() ? Math.min(0.4, vec3d.y / 2.0 + strength) : vec3d.y, vec3d.z / 2.0 - vec3d2.z);

        ci.cancel();
    }

    @Inject(
            method = "disablesShield",
            at = @At("RETURN"),
            cancellable = true
    )
    private void disablesShield(CallbackInfoReturnable<Boolean> cir) {
        if (getWeaponStack().getItem() instanceof ScytheItem) {
            cir.setReturnValue(true);
        }
    }
}
