package net.typho.beryllium.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;

public class GoldHopperBlock extends HopperBlock {
    public GoldHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GoldHopperBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, Beryllium.REDSTONE.GOLD_HOPPER_BLOCK_ENTITY, HopperBlockEntity::serverTick);
    }
}
