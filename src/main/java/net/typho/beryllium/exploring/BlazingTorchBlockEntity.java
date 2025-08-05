package net.typho.beryllium.exploring;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlazingTorchBlockEntity extends BlockEntity {
    public BlazingTorchBlockEntity(BlockPos pos, BlockState state) {
        super(Exploring.BLAZING_TORCH_BLOCK_ENTITY, pos, state);
    }
}
