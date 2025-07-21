package net.typho.beryllium.building.kiln;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Beryllium;

public class KilnEntity extends AbstractFurnaceBlockEntity {
    public KilnEntity(BlockPos pos, BlockState state) {
        super(Beryllium.BUILDING.KILN_BLOCK_ENTITY_TYPE, pos, state, Beryllium.BUILDING.KILN_RECIPE_TYPE);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.beryllium.building.kiln");
    }

    @Override
    protected int getFuelTime(ItemStack fuel) {
        return super.getFuelTime(fuel) / 2;
    }

    @Override
    protected KilnScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new KilnScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
