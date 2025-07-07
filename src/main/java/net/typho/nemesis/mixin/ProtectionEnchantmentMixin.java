package net.typho.nemesis.mixin;

import net.minecraft.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {
    @Inject(
            method = "getMaxLevel",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(5);
    }
}
