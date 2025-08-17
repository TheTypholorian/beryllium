package net.typho.beryllium.exploring;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class VoidFireBlock extends AbstractFireBlock {
    public static final MapCodec<VoidFireBlock> CODEC = createCodec(VoidFireBlock::new);

    @Override
    public MapCodec<VoidFireBlock> getCodec() {
        return CODEC;
    }

    public VoidFireBlock(AbstractBlock.Settings settings) {
        super(settings, 2.0F);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.isFireImmune() && entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(Exploring.SHATTERED, 15));
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        return this.canPlaceAt(state, world, pos) ? this.getDefaultState() : Blocks.AIR.getDefaultState();
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isVoidBase(world.getBlockState(pos.down()));
    }

    public static boolean isVoidBase(BlockState state) {
        return state.isIn(Exploring.VOID_FIRE_BASE_BLOCKS);
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }
}