package net.typho.beryllium.exploring;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class AlgaeBlock extends MultifaceGrowthBlock implements Fertilizable, Waterloggable {
    private static final Map<Direction, Property<Boolean>> PROPERTIES_FOR_DIRECTIONS = Util.make(Maps.newEnumMap(Direction.class), properties -> {
        properties.put(Direction.NORTH, Properties.UP);
        properties.put(Direction.EAST, Properties.EAST);
        properties.put(Direction.SOUTH, Properties.SOUTH);
        properties.put(Direction.WEST, Properties.WEST);
        properties.put(Direction.UP, Properties.UP);
        properties.put(Direction.DOWN, Properties.DOWN);
    });
    public static final BooleanProperty GENERATED = BooleanProperty.of("generated");
    public static final MapCodec<AlgaeBlock> CODEC = createCodec(AlgaeBlock::new);
    private final LichenGrower grower = new LichenGrower(this);

    public AlgaeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(GENERATED, false));
    }

    public static BlockState createFullState(BlockState state, WorldAccess world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.offset(direction);

            if (canGrowOn(world, direction, neighbor, world.getBlockState(neighbor))) {
                state = state.with(PROPERTIES_FOR_DIRECTIONS.get(direction), true);
            }
        }

        return state;
    }

    @Override
    protected MapCodec<? extends MultifaceGrowthBlock> getCodec() {
        return CODEC;
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }

    @Override
    public Item asItem() {
        return Exploring.ALGAE_ITEM;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return Direction.stream().anyMatch(direction -> grower.canGrow(state, world, pos, direction.getOpposite()));
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        grower.grow(state, world, pos, random);
    }

    @Override
    public boolean canGrowWithDirection(BlockView world, BlockState state, BlockPos pos, Direction direction) {
        if (canHaveDirection(direction) && (!state.isOf(this) || !hasDirection(state, direction))) {
            BlockPos blockPos = pos.offset(direction);
            BlockState on = world.getBlockState(blockPos);
            return (direction == Direction.DOWN && (on.isOf(Blocks.WATER) || (on.isOf(this) && on.getOrEmpty(Properties.WATERLOGGED).orElse(false)))) || canGrowOn(world, direction, blockPos, on);
        } else {
            return false;
        }
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.getOrEmpty(GENERATED).orElse(false)) {
            BlockState below = world.getBlockState(pos.down());

            if (state.getOrEmpty(Properties.WATERLOGGED).orElse(false)) {
                return below.isSolidBlock(world, pos.down());
            } else {
                return below.isOf(Blocks.WATER) || (below.isOf(this) && below.getOrEmpty(Properties.WATERLOGGED).orElse(false));
            }
        }

        boolean bl = false;

        for (Direction direction : DIRECTIONS) {
            if (hasDirection(state, direction)) {
                BlockPos blockPos = pos.offset(direction);
                BlockState wall = world.getBlockState(blockPos);

                if (!(canGrowOn(world, direction, blockPos, wall) || ((state.isOf(Blocks.AIR) || !state.getOrEmpty(Properties.WATERLOGGED).orElse(false)) && (wall.isOf(Blocks.WATER) || (wall.isOf(this) && wall.getOrEmpty(Properties.WATERLOGGED).orElse(false))) && direction == Direction.DOWN))) {
                    return false;
                }

                bl = true;
            }
        }

        return bl;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.WATERLOGGED, GENERATED);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }
}
