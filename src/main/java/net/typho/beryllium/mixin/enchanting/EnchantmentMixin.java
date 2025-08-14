package net.typho.beryllium.mixin.enchanting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.enchanting.EnchantmentInfo;
import net.typho.beryllium.enchanting.HasEnchantmentInfo;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
@Implements(@Interface(iface = HasEnchantmentInfo.class, prefix = "info$"))
public abstract class EnchantmentMixin {
    @Unique
    public EnchantmentInfo info = EnchantmentInfo.DEFAULT;

    public EnchantmentInfo info$getInfo() {
        return info;
    }

    public void info$setInfo(EnchantmentInfo info) {
        this.info = info;
    }

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
            text.append(ScreenTexts.SPACE).append(Enchanting.toRomanNumeral(level));
        }

        cir.setReturnValue(text);
    }

    @Inject(
            method = "isAcceptableItem",
            at = @At("RETURN"),
            cancellable = true
    )
    private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment ench = (Enchantment) (Object) this;

        if (stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK)) {
            cir.setReturnValue(true);
        }

        if (cir.getReturnValue()) {
            if (!Enchanting.canFitEnchantment(stack, ench)) {
                cir.setReturnValue(false);
            } else {
                for (RegistryEntry<Enchantment> enchantment : stack.getEnchantments().getEnchantments()) {
                    if (ench == enchantment.value()) {
                        cir.setReturnValue(true);
                        break;
                    }

                    if (enchantment.value().exclusiveSet().stream().anyMatch(entry -> entry.value() == ench) || ench.exclusiveSet().contains(enchantment)) {
                        cir.setReturnValue(false);
                        break;
                    }
                }
            }
        }
    }
}
