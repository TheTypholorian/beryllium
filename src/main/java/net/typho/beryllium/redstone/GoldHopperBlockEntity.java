package net.typho.beryllium.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Beryllium;

public class GoldHopperBlockEntity extends HopperBlockEntity {
    public GoldHopperBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public int size() {
        return 6;
    }

    public Item filter() {
        return getStack(5).getItem();
    }

    public boolean testFilter(ItemStack stack) {
        return stack.isOf(filter()) || stack.isEmpty();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 5 || testFilter(stack)) {
            super.setStack(slot, stack);
        }
    }

    @Override
    public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
        if (slot == 5) {
            return false;
        }

        if (!testFilter(stack)) {
            return false;
        }

        return super.canTransferTo(hopperInventory, slot, stack);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 5) {
            return false;
        }

        return testFilter(stack);
    }

    @Override
    public boolean supports(BlockState state) {
        return getType().supports(state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return Beryllium.REDSTONE.GOLD_HOPPER_BLOCK_ENTITY;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GoldHopperScreenHandler(Beryllium.REDSTONE.GOLD_HOPPER_SCREEN_HANDLER_TYPE, syncId, playerInventory, this);
    }
}
