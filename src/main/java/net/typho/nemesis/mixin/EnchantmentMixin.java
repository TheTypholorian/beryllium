package net.typho.nemesis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(
            method = "getName",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getName(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir) {
        MutableText text = enchantment.value().description().copy();

        if (enchantment.isIn(EnchantmentTags.CURSE)) {
            Texts.setStyleIfAbsent(text, Style.EMPTY.withColor(Formatting.RED));
        } else {
            Texts.setStyleIfAbsent(text, Style.EMPTY.withColor(Formatting.GRAY));
        }

        if (level != 1 || enchantment.value().getMaxLevel() != 1) {
            text.append(ScreenTexts.SPACE).append(Nemesis.toRomanNumeral(level));
        }

        text.append(ScreenTexts.SPACE).append("(" + Nemesis.getEnchantmentPoints(enchantment.value()) + " pts)");

        cir.setReturnValue(text);
    }

    @Inject(
            method = "isAcceptableItem",
            at = @At("RETURN"),
            cancellable = true
    )
    private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (Nemesis.getEnchantmentPoints(stack) + Nemesis.getEnchantmentPoints((Enchantment) (Object) this) > Nemesis.getMaxEnchantmentPoints(stack)) {
                cir.setReturnValue(false);
            }
        }
    }
}
