package net.typho.nemesis.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin {
    @Shadow
    protected abstract void crystalDestroyed(DamageSource source);

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        EndCrystalEntity crystal = (EndCrystalEntity) (Object) this;

        if (crystal.isInvulnerableTo(source)) {
            cir.setReturnValue(false);
        } else if (source.getAttacker() instanceof EnderDragonEntity) {
            cir.setReturnValue(false);
        } else {
            if (!crystal.isRemoved() && !crystal.getWorld().isClient) {
                crystal.remove(Entity.RemovalReason.KILLED);

                DamageSource damageSource = source.getAttacker() != null ? crystal.getDamageSources().explosion(crystal, source.getAttacker()) : null;
                crystal.getWorld().createExplosion(crystal, damageSource, null, crystal.getX(), crystal.getY(), crystal.getZ(), 4f, true, World.ExplosionSourceType.BLOCK);

                crystalDestroyed(source);
            }

            cir.setReturnValue(true);
        }
    }
}
