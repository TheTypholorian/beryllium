package net.typho.beryllium.exploring;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class FireflyBottleBlock extends LanternBlock {
    public static final VoxelShape STANDING_PBOX = Block.createCuboidShape(5, 0, 5, 11, 7, 11),
            HANGING_PBOX = Block.createCuboidShape(5, 1, 5, 11, 8, 11);

    public FireflyBottleBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }
}
