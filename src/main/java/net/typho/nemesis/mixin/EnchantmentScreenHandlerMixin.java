package net.typho.nemesis.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        return 5;
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(intValue = 35, ordinal = 0)
    )
    private int lapisSlot(int constant) {
        return 23;
    }

    @Inject(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/EnchantmentScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void recipeSlot(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        addSlot(new Slot(inventory, 2, 41, 47) {
            @Override
            public @NotNull Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of("item/empty_slot_amethyst_shard"));
            }
        });
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

    @Inject(
            method = "quickMove",
            at = @At("HEAD"),
            cancellable = true
    )
    public void quickMove(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);

        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0 || slot == 1 || slot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            } else if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                if (!this.insertItem(itemStack2, 1, 2, true)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            } else {
                if (this.slots.get(0).hasStack() || !this.slots.get(0).canInsert(itemStack2)) {
                    if (this.slots.get(2).hasStack() || !this.slots.get(2).canInsert(itemStack2)) {
                        cir.setReturnValue(ItemStack.EMPTY);
                    } else {
                        this.slots.get(2).setStack(itemStack2);
                        slot2.setStack(ItemStack.EMPTY);
                    }
                } else {
                    ItemStack itemStack3 = itemStack2.copyWithCount(1);
                    itemStack2.decrement(1);
                    this.slots.get(0).setStack(itemStack3);
                }
            }

            if (cir.getReturnValue() != null) {
                if (itemStack2.isEmpty()) {
                    slot2.setStack(ItemStack.EMPTY);
                } else {
                    slot2.markDirty();
                }

                if (itemStack2.getCount() == itemStack.getCount()) {
                    cir.setReturnValue(ItemStack.EMPTY);
                } else {
                    slot2.onTakeItem(player, itemStack2);
                }
            }
        }

        if (cir.getReturnValue() != null) {
            cir.setReturnValue(itemStack);
        }
    }

    @Mixin(targets = "net.minecraft.screen.EnchantmentScreenHandler$2")
    public abstract static class SubMixin2 extends Slot {
        public SubMixin2(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.getItem().isEnchantable(stack);
        }

        @Override
        public @NotNull Pair<Identifier, Identifier> getBackgroundSprite() {
            return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.ofVanilla("item/empty_slot_sword"));
        }
    }
}
