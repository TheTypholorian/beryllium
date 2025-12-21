package net.typho.beryllium.mixin.tooltips;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.typho.beryllium.tooltips.ItemIsMaterialStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
                    ordinal = 6
            )
    )
    private void getTooltipLines(
            Item.TooltipContext context,
            @Nullable Player player,
            TooltipFlag tooltipFlag,
            CallbackInfoReturnable<List<Component>> cir,
            @Local Consumer<Component> out
    ) {
        if (ItemIsMaterialStorage.test(getItem())) {
            out.accept(Component.translatable("tooltip.beryllium.is_material").withStyle(ChatFormatting.BLUE));
        }
    }
}
