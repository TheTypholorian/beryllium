package net.typho.beryllium.mixin.crossbows;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.ModConfig;
import net.typho.beryllium.crossbows.BurstCrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @ModifyArg(
            method = "getChargeDuration",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;floor(F)I"
            )
    )
    private static float getChargeDuration(float value, @Local(argsOnly = true) ItemStack stack) {
        return stack.getItem() instanceof BurstCrossbowItem ? value * ModConfig.instance.crossbows.burstCrossbow.chargeDurationMultiplier.get() : value;
    }
}
