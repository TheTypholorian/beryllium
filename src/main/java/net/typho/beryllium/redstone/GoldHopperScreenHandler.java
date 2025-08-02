package net.typho.beryllium.redstone;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoldHopperScreenHandler extends ScreenHandler {
    public static final int SLOT_COUNT = 6;
    private final Inventory inventory;

    protected GoldHopperScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(Redstone.GOLD_HOPPER_SCREEN_HANDLER_TYPE, syncId, playerInventory);
    }

    protected GoldHopperScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory) {
        this(type, syncId, playerInventory, new SimpleInventory(SLOT_COUNT));
    }

    protected GoldHopperScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        this.inventory = inventory;
        checkSize(inventory, SLOT_COUNT);
        inventory.onOpen(playerInventory.player);

        for (int j = 0; j < SLOT_COUNT - 1; j++) {
            addSlot(new Slot(inventory, j, 44 + j * 18, 20) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return inventory instanceof GoldHopperBlockEntity hopper ? hopper.testFilter(stack) : stack.isOf(inventory.getStack(5).getItem());
                }
            });
        }

        addSlot(new Slot(inventory, SLOT_COUNT - 1, 80, 38) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public @NotNull Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.ofVanilla("item/empty_slot_redstone_dust"));
            }
        });

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51 + 18));
            }
        }

        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(playerInventory, j, 8 + j * 18, 109 + 18));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = slots.get(slot);

        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();

            if (slot < inventory.size()) {
                if (!insertItem(itemStack2, inventory.size(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(itemStack2, 0, inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }

        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        inventory.onClose(player);
    }
}
