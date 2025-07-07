package net.typho.nemesis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(
            method = "set",
            at = @At("HEAD")
    )
    private static void set(Map<Enchantment, Integer> enchantments, ItemStack stack, CallbackInfo ci) {
        if (enchantments.size() > Nemesis.MAX_ENCHANTMENTS) {
            int[] i = {0};
            enchantments.keySet().removeIf(level -> i[0]++ >= Nemesis.MAX_ENCHANTMENTS);
        }
    }
}
