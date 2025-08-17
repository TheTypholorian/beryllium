package net.typho.beryllium.mixin.armor;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.RangedWeaponItem;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @ModifyArg(
            method = "shootAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/RangedWeaponItem;shoot(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/projectile/ProjectileEntity;IFFFLnet/minecraft/entity/LivingEntity;)V"
            ),
            index = 3
    )
    private float shoot(float speed, @Local(ordinal = 0, argsOnly = true) LivingEntity shooter) {
        return speed * (float) shooter.getAttributeValue(Armor.GENERIC_RANGED_SPEED);
    }
}
