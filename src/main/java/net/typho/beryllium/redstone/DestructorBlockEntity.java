package net.typho.beryllium.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DestructorBlockEntity extends BlockEntity {
    protected BlockState target = null;
    protected float progress = 0;

    public DestructorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public DestructorBlockEntity(BlockPos pos, BlockState state) {
        super(Redstone.DESTRUCTOR_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putFloat("progress", progress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        progress = nbt.getFloat("progress");
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            BlockPos targetPos = pos.offset(state.get(FacingBlock.FACING));
            BlockState target = world.getBlockState(targetPos);

            if (this.target != null && !this.target.equals(target)) {
                this.progress = 0;
                world.setBlockBreakingInfo(-1, targetPos, -1);
            } else {
                if (world.isReceivingRedstonePower(pos)) {
                    float hardness = state.getHardness(world, targetPos);

                    if (hardness < 0) {
                        this.progress = -1;
                        world.setBlockBreakingInfo(-1, targetPos, -1);
                    } else {
                        this.progress += 6 / hardness / 30;
                        world.setBlockBreakingInfo(-1, targetPos, (int) (this.progress * 10));

                        if (this.progress >= 1) {
                            world.breakBlock(targetPos, true);
                            this.progress = 0;
                        }
                    }
                } else {
                    this.progress = -1;
                    world.setBlockBreakingInfo(-1, targetPos, -1);
                }
            }

            this.target = target;
        }
    }
}
