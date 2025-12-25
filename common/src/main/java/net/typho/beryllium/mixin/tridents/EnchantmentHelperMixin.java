package net.typho.beryllium.mixin.tridents;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.typho.beryllium.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(
            method = "getTridentSpinAttackStrength",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getTridentSpinAttackStrength(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        if (ModConfig.instance.trident.builtinRiptide && stack.is(Items.TRIDENT)) {
            cir.setReturnValue(3f);
        }
    }
}
