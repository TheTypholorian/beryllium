package net.typho.beryllium.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(
            method = "isDamageable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageable(CallbackInfoReturnable<Boolean> cir) {
        if (ServerConfig.DURABILITY_REMOVAL.get() || getItem() instanceof ShieldItem) {
            cir.setReturnValue(false);
        }
    }
}
