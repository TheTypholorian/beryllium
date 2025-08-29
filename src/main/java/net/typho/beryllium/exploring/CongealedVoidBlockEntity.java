package net.typho.beryllium.exploring;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CongealedVoidBlockEntity extends BlockEntity {
    public CongealedVoidBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CongealedVoidBlockEntity(BlockPos pos, BlockState state) {
        this(Exploring.CONGEALED_VOID_BLOCK_ENTITY, pos, state);
    }

    public boolean shouldDrawSide(Direction side) {
        return !(world.getBlockState(getPos().offset(side)).getBlock() instanceof CongealedVoidBlock);
    }
}
