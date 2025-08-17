package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(
            method = "onEntityHit",
            at = @At(value = "STORE", ordinal = 0)
    )
    private double bonusDamage(double d) {
        if (getOwner() instanceof LivingEntity living) {
            d += Armor.bonusRangedDamage(living);
        }

        return d;
    }

    @ModifyVariable(
            method = "setVelocity",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private double modifyX(double d) {
        if (getOwner() instanceof LivingEntity living) {
            d *= 1 + Armor.bonusRangedSpeed(living);
        }

        return d;
    }

    @ModifyVariable(
            method = "setVelocity",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true
    )
    private double modifyY(double d) {
        if (getOwner() instanceof LivingEntity living) {
            d *= 1 + Armor.bonusRangedSpeed(living);
        }

        return d;
    }

    @ModifyVariable(
            method = "setVelocity",
            at = @At("HEAD"),
            ordinal = 2,
            argsOnly = true
    )
    private double modifyZ(double d) {
        if (getOwner() instanceof LivingEntity living) {
            d *= 1 + Armor.bonusRangedSpeed(living);
        }

        return d;
    }
}
