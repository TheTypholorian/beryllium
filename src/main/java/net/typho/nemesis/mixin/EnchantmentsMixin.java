package net.typho.nemesis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantments.class)
public class EnchantmentsMixin {
    @Inject(
            method = "register",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void register(String name, Enchantment enchantment, CallbackInfoReturnable<Enchantment> cir) {
        switch (name) {
            case "unbreaking", "mending": {
                cir.setReturnValue(enchantment);
            }
        }
    }
}
