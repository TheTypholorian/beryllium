package net.typho.nemesis.mixin;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.enchantment.EnchantmentTarget$11")
public class EnchantmentTargetMixin {
    @Inject(
            method = "isAcceptableItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isAcceptableItem(Item item, CallbackInfoReturnable<Boolean> cir) {
        if (item instanceof AxeItem) {
            cir.setReturnValue(true);
        }
    }
}
