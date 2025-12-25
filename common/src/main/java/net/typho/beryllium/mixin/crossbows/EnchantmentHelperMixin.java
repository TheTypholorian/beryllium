package net.typho.beryllium.mixin.crossbows;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.typho.beryllium.ModConfig;
import net.typho.beryllium.crossbows.BurstCrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyVariable(
            method = "processProjectileCount",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static int processProjectileCount(int projectileCount, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof BurstCrossbowItem) {
            return ModConfig.instance.crossbows.burstCrossbow.numProjectiles.get();
        }

        return projectileCount;
    }

    @ModifyVariable(
            method = "processProjectileSpread",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static float processProjectileSpread(float spread, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof BurstCrossbowItem) {
            return spread + ModConfig.instance.crossbows.burstCrossbow.spread.get();
        }

        return spread;
    }
}
