package net.typho.nemesis.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.typho.nemesis.Nemesis;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Inject(
            method = "shoot",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
        if (!world.isClient && projectile.getItem() instanceof EndCrystalItem) {
            Nemesis.MovingEndCrystalEntity entity = new Nemesis.MovingEndCrystalEntity(world, shooter.getX(), shooter.getEyeY() - 0.15f, shooter.getZ());
            entity.setShowBottom(false);

            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double)(simulated * (float) (Math.PI / 180.0)), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0F);
            Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
            entity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);

            crossbow.damage(3, shooter, e -> e.sendToolBreakStatus(hand));
            world.spawnEntity(entity);
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
            ci.cancel();
        }
    }

    @Inject(
            method = "getProjectiles()Ljava/util/function/Predicate;",
            at = @At("TAIL"),
            cancellable = true
    )
    private void shoot(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        cir.setReturnValue(stack -> stack.isIn(Nemesis.CROSSBOW_PROJECTILES));
    }
}
