package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.exploring.VoidFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin extends ExplosiveProjectileEntity {
    protected DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    public DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> type, double x, double y, double z, Vec3d velocity, World world) {
        super(type, x, y, z, velocity, world);
    }

    public DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> type, LivingEntity owner, Vec3d velocity, World world) {
        super(type, owner, velocity, world);
    }

    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/DragonFireballEntity;discard()V"
            )
    )
    private void onCollision(HitResult hit, CallbackInfo ci, @Local AreaEffectCloudEntity cloud) {
        BlockPos origin = cloud.getBlockPos();

        for (int x = origin.getX() - (int) cloud.getRadius(); x <= origin.getX() + (int) cloud.getRadius(); x++) {
            for (int y = origin.getY() - (int) cloud.getRadius(); y <= origin.getY() + (int) cloud.getRadius(); y++) {
                for (int z = origin.getZ() - (int) cloud.getRadius(); z <= origin.getZ() + (int) cloud.getRadius(); z++) {
                    BlockPos place = new BlockPos(x, y, z);

                    if (place.isWithinDistance(origin, cloud.getRadius())) {
                        if (
                                getWorld().getBlockState(place).isReplaceable() &&
                                        VoidFireBlock.isVoidBase(getWorld().getBlockState(place.down())) &&
                                        getWorld().getRandom().nextFloat() > 0.5f
                        ) {
                            getWorld().setBlockState(place, Exploring.VOID_FIRE.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/AreaEffectCloudEntity;addEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)V"
            )
    )
    private void addEffect(HitResult hitResult, CallbackInfo ci, @Local AreaEffectCloudEntity cloud) {
        cloud.addEffect(new StatusEffectInstance(Exploring.SHATTERED, 15));
    }
}
