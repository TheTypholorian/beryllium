package net.typho.beryllium.mixin.trims;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.SmithingTemplateItem;
import net.typho.beryllium.ModConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    public SmithingMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @WrapOperation(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/SmithingMenu;shrinkStackInSlot(I)V",
                    ordinal = 0
            )
    )
    private void onTake(SmithingMenu instance, int index, Operation<Void> original) {
        if (ModConfig.instance.smithing.templatesConsumable || !(getSlot(index).getItem().getItem() instanceof SmithingTemplateItem)) {
            original.call(instance, index);
        }
    }
}
