package net.typho.beryllium.mixin.remove_enchantments;

import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(
            method = "isEnchantable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.instance.enchantmentsEnabled) {
            cir.setReturnValue(false);
        }
    }
}
