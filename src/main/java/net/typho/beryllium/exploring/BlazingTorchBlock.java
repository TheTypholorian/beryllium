package net.typho.beryllium.exploring;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlazingTorchBlock extends TorchBlock implements BlockEntityProvider {
    public BlazingTorchBlock(SimpleParticleType particle, Settings settings) {
        super(particle, settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlazingTorchBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlazingTorchBlock::tick;
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        burn(world, pos);
    }

    public static void burn(World world, BlockPos pos) {
        for (Entity mob : world.getOtherEntities(null, new Box(pos).expand(8), e -> e instanceof Monster)) {
            mob.setOnFireFor(4);
        }
    }
}
