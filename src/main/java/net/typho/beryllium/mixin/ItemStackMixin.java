package net.typho.beryllium.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.typho.beryllium.Beryllium;
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
            method = "isDamageable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 4
            )
    )
    private void appendTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> text) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.currentScreen instanceof EnchantmentScreen || client.currentScreen instanceof AnvilScreen || client.currentScreen instanceof GrindstoneScreen) {
            ItemStack stack = (ItemStack) (Object) this;

            if (stack.getItem().getEnchantability() != 0) {
                text.add(Text.literal(Beryllium.getUsedEnchCapacity(stack) + " / " + Beryllium.getMaxEnchCapacity(stack)).setStyle(Style.EMPTY.withColor(new Color(167, 85, 255).getRGB())));
            }
        }
    }
}
