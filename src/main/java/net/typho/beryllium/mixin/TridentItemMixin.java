package net.typho.beryllium.mixin;

import net.minecraft.item.ToolMaterials;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @Inject(
            method = "getEnchantability",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getEnchantability(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ToolMaterials.NETHERITE.getEnchantability());
    }
}
