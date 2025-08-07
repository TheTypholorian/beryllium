package net.typho.beryllium.mixin.combat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.combat.ScytheItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    @NotNull
    public abstract ItemStack getWeaponStack();

    @Shadow
    protected ItemStack activeItemStack;

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

    @Redirect(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;takeShieldHit(Lnet/minecraft/entity/LivingEntity;)V"
            )
    )
    private void damageShieldHit(LivingEntity instance, LivingEntity attacker, @Local(ordinal = 2) float amount) {
        ItemStack shield = activeItemStack;
        float durability = Combat.shieldDurability(shield) - amount;

        if (durability <= 0) {
            durability = Beryllium.SERVER_CONFIG.shieldMaxDurability.get();

            if ((Object) this instanceof PlayerEntity player) {
                player.disableShield();
            }
        }

        shield.set(Combat.SHIELD_DURABILITY, durability);
    }

    @Inject(
            method = "takeShieldHit",
            at = @At("HEAD")
    )
    private void takeShieldHit(LivingEntity attacker, CallbackInfo ci) {
        ItemStack shield = activeItemStack;
        float durability = Combat.shieldDurability(shield) - 1;

        if (durability <= 0) {
            durability = Beryllium.SERVER_CONFIG.shieldMaxDurability.get();

            if ((Object) this instanceof PlayerEntity player) {
                player.disableShield();
            }
        }

        shield.set(Combat.SHIELD_DURABILITY, durability);
    }

    @Inject(
            method = "getMovementSpeed()F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void bonusSpeed(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() + Combat.bonusSpeed((LivingEntity) (Object) this));
    }

    @ModifyVariable(
            method = "travel(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"
            ),
            ordinal = 0
    )
    private float bonusSwimmingSpeed(float f) {
        return f + Combat.bonusSwimmingSpeed((LivingEntity) (Object) this);
    }

    @Inject(
            method = "getArmor",
            at = @At("RETURN"),
            cancellable = true
    )
    private void bonusArmor(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() + Combat.bonusArmor((LivingEntity) (Object) this));
    }

    @WrapOperation(
            method = "takeKnockback",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
            )
    )
    private double bonusKnockbackResistance(LivingEntity instance, RegistryEntry<EntityAttribute> attribute, Operation<Double> original) {
        return original.call(instance, attribute) + Combat.bonusKnockbackResistance((LivingEntity) (Object) this);
    }

    @WrapOperation(
            method = "getKnockbackAgainst",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
            )
    )
    private double bonusKnockback(LivingEntity instance, RegistryEntry<EntityAttribute> attribute, Operation<Double> original) {
        return original.call(instance, attribute) + Combat.bonusAttackKnockback((LivingEntity) (Object) this);
    }

    @Inject(
            method = "getJumpBoostVelocityModifier",
            at = @At("RETURN"),
            cancellable = true
    )
    private void bonusJumpHeight(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() + Combat.bonusJumpHeight((LivingEntity) (Object) this));
    }
}
