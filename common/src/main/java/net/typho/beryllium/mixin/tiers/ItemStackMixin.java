package net.typho.beryllium.mixin.tiers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.ModItemComponents;
import net.typho.beryllium.tiers.Tier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
    @WrapOperation(
            method = "getHoverName",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
            )
    )
    private Component getHoverName(Item instance, ItemStack stack, Operation<Component> original) {
        Component name = original.call(instance, stack);
        Tier tier = stack.get(ModItemComponents.tier.get());

        if (tier != null) {
            name = Component.empty().append(tier.getText()).append(" ").append(name);
        }

        return name;
    }
}
