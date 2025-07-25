package net.typho.beryllium.mixin.combat;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
            method = "isDamageable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
