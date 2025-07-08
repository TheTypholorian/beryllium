package net.typho.nemesis.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract int getMaxDamage();

    @Inject(
            method = "isDamageable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(
            method = "getDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getDamage(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(getMaxDamage());
    }

    @Inject(
            method = "setDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void setDamage(int damage, CallbackInfo ci) {
        ci.cancel();
    }
}
