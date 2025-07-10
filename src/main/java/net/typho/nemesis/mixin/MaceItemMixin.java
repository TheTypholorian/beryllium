package net.typho.nemesis.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.MaceItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.item.MaceItem.shouldDealAdditionalDamage;

@Mixin(MaceItem.class)
public class MaceItemMixin {
    @Inject(
            method = "getBonusAttackDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        if (damageSource.getSource() instanceof LivingEntity livingEntity) {
            if (!shouldDealAdditionalDamage(livingEntity)) {
                cir.setReturnValue(0f);
            } else {
                float height = livingEntity.fallDistance;

                if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
                    height += EnchantmentHelper.getSmashDamagePerFallenBlock(serverWorld, livingEntity.getWeaponStack(), target, damageSource, 0);
                }

                cir.setReturnValue((float) (10 * Math.cbrt(height / 20)));
            }
        } else {
            cir.setReturnValue(0f);
        }
    }
}
