package net.typho.beryllium.mixin.remove_durability;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(
            method = "isDamageableItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
