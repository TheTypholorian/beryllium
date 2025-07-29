package net.typho.beryllium.mixin.exploring;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {
    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tick(CallbackInfo ci) {
        for (Entity projectile : getWorld().getOtherEntities(this, getBoundingBox().expand(16), e -> e instanceof ProjectileEntity)) {
            projectile.setVelocity(projectile.getPos().subtract(getPos()).normalize());
            projectile.velocityModified = true;

            Vec3d particleVel = projectile.getVelocity().multiply(0.2);
            getWorld().addParticle(ParticleTypes.DRAGON_BREATH, projectile.getX(), projectile.getY(), projectile.getZ(), particleVel.x, particleVel.y, particleVel.z);
        }
    }
}
