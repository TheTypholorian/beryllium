package net.typho.beryllium.mixin.armor;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.util.Identifier;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin {
    @Inject(
            method = "of(Lnet/minecraft/util/Identifier;[Lnet/minecraft/resource/featuretoggle/FeatureFlag;)Lnet/minecraft/item/SmithingTemplateItem;",
            at = @At("RETURN")
    )
    private static void of(Identifier pattern, FeatureFlag[] requiredFeatures, CallbackInfoReturnable<SmithingTemplateItem> cir) {
        Armor.SMITHING_TEMPLATE_PATTERNS.put(cir.getReturnValue(), pattern);
    }
}
