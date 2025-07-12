package net.typho.nemesis.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
    @Shadow
    @Final
    private Inventory inventory;

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(intValue = 15, ordinal = 0)
    )
    private int equipmentSlot(int constant) {
        return constant + 8;
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(intValue = 35, ordinal = 0)
    )
    private int lapisSlot(int constant) {
        return constant + 6;
    }

    @Inject(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/EnchantmentScreenHandler;addProperty(Lnet/minecraft/screen/Property;)Lnet/minecraft/screen/Property;",
                    ordinal = 0
            )
    )
    private void recipeSlot(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        addSlot(new Slot(inventory, 2, 5, 47));
    }

    @Redirect(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraft/screen/slot/Slot;"
            )
    )
    private Slot newSlot(Inventory inventory, int index, int x, int y) {
        if (index > 1) {
            index++;
        }

        return new Slot(inventory, index, x, y);
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(
                    intValue = 2,
                    ordinal = 0
            )
    )
    private int changeInventorySize(int original) {
        return original + 1;
    }
}
