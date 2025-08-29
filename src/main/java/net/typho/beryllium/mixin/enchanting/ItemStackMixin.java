package net.typho.beryllium.mixin.enchanting;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.typho.beryllium.config.ServerConfig;
import net.typho.beryllium.enchanting.Enchanting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 4
            )
    )
    @Environment(EnvType.CLIENT)
    private void appendTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> text) {
        if (ServerConfig.ENCHANTMENT_CAPACITY.get() && player != null) {
            if (player.currentScreenHandler instanceof EnchantmentScreenHandler || player.currentScreenHandler instanceof AnvilScreenHandler || player.currentScreenHandler instanceof GrindstoneScreenHandler) {
                ItemStack stack = (ItemStack) (Object) this;

                if (stack.getItem().getEnchantability() != 0) {
                    text.add(Text.literal(Enchanting.getUsedEnchCapacity(stack) + " / " + Enchanting.getMaxEnchCapacity(stack)).setStyle(Style.EMPTY.withColor(new Color(167, 85, 255).getRGB())));
                }
            }
        }
    }
}
