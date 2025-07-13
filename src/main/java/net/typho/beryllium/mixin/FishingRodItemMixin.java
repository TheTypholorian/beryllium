package net.typho.beryllium.mixin;

import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
    @Inject(
            method = "getEnchantability",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getEnchantability(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ToolMaterials.NETHERITE.getEnchantability());
    }
}
