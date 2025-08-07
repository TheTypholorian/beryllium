package net.typho.beryllium.mixin.combat;

import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.Type.class)
public class ArmorItemTypeMixin {
    @Inject(
            method = "isTrimmable",
            at = @At("RETURN"),
            cancellable = true
    )
    private void isTrimmable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || (Object) this == ArmorItem.Type.BODY);
    }
}
