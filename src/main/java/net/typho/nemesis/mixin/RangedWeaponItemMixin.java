package net.typho.nemesis.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @Shadow
    @Mutable
    @Final
    public static Predicate<ItemStack> CROSSBOW_HELD_PROJECTILES;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        CROSSBOW_HELD_PROJECTILES = CROSSBOW_HELD_PROJECTILES.or(stack -> stack.isOf(Items.END_CRYSTAL));
    }

    @Inject(method = "createArrowEntity", at = @At("HEAD"), cancellable = true)
    protected void createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir) {
        Item item = projectileStack.getItem();

        if (item instanceof ArrowItem arrowItem) {
            PersistentProjectileEntity proj = arrowItem.createArrow(world, projectileStack, shooter, weaponStack);

            if (critical) {
                proj.setCritical(true);
            }

            cir.setReturnValue(proj);
        } else if (item instanceof ProjectileItem projItem) {
            ProjectileEntity proj = projItem.createEntity(world, shooter.getEyePos().subtract(0, 0.15, 0), projectileStack, Direction.NORTH);

            if (critical && proj instanceof PersistentProjectileEntity persistent) {
                persistent.setCritical(true);
            }

            cir.setReturnValue(proj);
        } else {
            PersistentProjectileEntity proj = ((ArrowItem) Items.ARROW).createArrow(world, projectileStack, shooter, weaponStack);

            if (critical) {
                proj.setCritical(true);
            }

            cir.setReturnValue(proj);
        }
    }
}
