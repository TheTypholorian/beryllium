package net.typho.beryllium.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.typho.beryllium.SweepingItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
    @Inject(
            method = "raycast",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void raycast(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double maxDistance, CallbackInfoReturnable<EntityHitResult> cir) {
        World world = entity.getWorld();
        double d = maxDistance;
        Entity entity2 = null;
        Vec3d vec3d = null;
        float margin = 0;

        if (entity instanceof PlayerEntity player) {
            ItemStack held = player.getMainHandStack();

            if (held.getItem() instanceof SweepingItem sweep) {
                margin += sweep.sweep(player, held);
            }
        }

        for (Entity entity3 : world.getOtherEntities(entity, box, predicate)) {
            Box box2 = entity3.getBoundingBox().expand(entity3.getTargetingMargin() + margin);
            Optional<Vec3d> optional = box2.raycast(min, max);
            if (box2.contains(min)) {
                if (d >= 0.0) {
                    entity2 = entity3;
                    vec3d = optional.orElse(min);
                    d = 0.0;
                }
            } else if (optional.isPresent()) {
                Vec3d vec3d2 = optional.get();
                double e = min.squaredDistanceTo(vec3d2);
                if (e < d || d == 0.0) {
                    if (entity3.getRootVehicle() == entity.getRootVehicle()) {
                        if (d == 0.0) {
                            entity2 = entity3;
                            vec3d = vec3d2;
                        }
                    } else {
                        entity2 = entity3;
                        vec3d = vec3d2;
                        d = e;
                    }
                }
            }
        }

        cir.setReturnValue(entity2 == null ? null : new EntityHitResult(entity2, vec3d));
    }
}
