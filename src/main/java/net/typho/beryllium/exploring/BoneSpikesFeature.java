package net.typho.beryllium.exploring;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Thickness;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.BasaltColumnsFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.Nullable;

public class BoneSpikesFeature extends Feature<BasaltColumnsFeatureConfig> {
    private static final ImmutableList<Block> CANNOT_REPLACE_BLOCKS = ImmutableList.of(
            Blocks.LAVA,
            Blocks.BEDROCK,
            Blocks.MAGMA_BLOCK,
            Blocks.SOUL_SAND,
            Blocks.NETHER_BRICKS,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.NETHER_BRICK_STAIRS,
            Blocks.NETHER_WART,
            Blocks.CHEST,
            Blocks.SPAWNER
    );

    public BoneSpikesFeature(Codec<BasaltColumnsFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BasaltColumnsFeatureConfig> context) {
        int i = context.getGenerator().getSeaLevel();
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        BasaltColumnsFeatureConfig basaltColumnsFeatureConfig = context.getConfig();

        int j = basaltColumnsFeatureConfig.getHeight().get(random);
        boolean bl = random.nextFloat() < 0.9F;
        int k = Math.min(j, bl ? 5 : 8);
        int l = bl ? 50 : 15;
        boolean bl2 = false;

        for (BlockPos blockPos2 : BlockPos.iterateRandomly(
                random, l, blockPos.getX() - k, blockPos.getY(), blockPos.getZ() - k, blockPos.getX() + k, blockPos.getY(), blockPos.getZ() + k
        )) {
            int m = j - blockPos2.getManhattanDistance(blockPos);
            if (m >= 0) {
                bl2 |= this.placeBasaltColumn(structureWorldAccess, i, blockPos2, m, basaltColumnsFeatureConfig.getReach().get(random));
            }
        }

        return bl2;
    }

    private boolean placeBasaltColumn(WorldAccess world, int seaLevel, BlockPos pos, int height, int reach) {
        boolean bl = false;

        for (BlockPos blockPos : BlockPos.iterate(pos.getX() - reach, pos.getY(), pos.getZ() - reach, pos.getX() + reach, pos.getY(), pos.getZ() + reach)) {
            int i = blockPos.getManhattanDistance(pos);
            BlockPos blockPos2 = isAirOrLavaOcean(world, seaLevel, blockPos)
                    ? moveUpToGround(world, seaLevel, blockPos.mutableCopy(), i)
                    : moveDownToAir(world, blockPos.mutableCopy(), i);
            if (blockPos2 != null) {
                int j = height - i / 2;

                for (BlockPos.Mutable mutable = blockPos2.mutableCopy(); j >= 0; j--) {
                    if (isAirOrLavaOcean(world, seaLevel, mutable)) {
                        BlockState place = world.getBlockState(mutable).isOf(Blocks.LAVA) ? Blocks.BONE_BLOCK.getDefaultState() :
                                switch (j) {
                                    case 0 -> Exploring.POINTED_BONE.getDefaultState()
                                            .with(PointedBoneBlock.VERTICAL_DIRECTION, Direction.DOWN)
                                            .with(PointedBoneBlock.THICKNESS, Thickness.TIP);
                                    case 1 -> Exploring.POINTED_BONE.getDefaultState()
                                            .with(PointedBoneBlock.VERTICAL_DIRECTION, Direction.DOWN)
                                            .with(PointedBoneBlock.THICKNESS, Thickness.FRUSTUM);
                                    case 2 -> Exploring.POINTED_BONE.getDefaultState()
                                            .with(PointedBoneBlock.VERTICAL_DIRECTION, Direction.DOWN)
                                            .with(PointedBoneBlock.THICKNESS, Thickness.MIDDLE);
                                    case 3 -> Exploring.POINTED_BONE.getDefaultState()
                                            .with(PointedBoneBlock.VERTICAL_DIRECTION, Direction.DOWN)
                                            .with(PointedBoneBlock.THICKNESS, Thickness.BASE);
                                    default -> Blocks.BONE_BLOCK.getDefaultState();
                                };

                        this.setBlockState(world, mutable, place);
                        mutable.move(Direction.DOWN);
                        bl = true;
                    } else {
                        if (!world.getBlockState(mutable).isOf(Blocks.BONE_BLOCK)) {
                            break;
                        }

                        mutable.move(Direction.DOWN);
                    }
                }
            }
        }

        return bl;
    }

    @Nullable
    private static BlockPos moveUpToGround(WorldAccess world, int seaLevel, BlockPos.Mutable mutablePos, int distance) {
        while (mutablePos.getY() < world.getTopY()) {
            if (canPlaceAt(world, seaLevel, mutablePos)) {
                return mutablePos;
            }

            mutablePos.move(Direction.UP);
        }

        return null;
    }

    private static boolean canPlaceAt(WorldAccess world, int seaLevel, BlockPos.Mutable mutablePos) {
        if (!isAirOrLavaOcean(world, seaLevel, mutablePos)) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(mutablePos.move(Direction.UP));
            mutablePos.move(Direction.DOWN);
            return !blockState.isAir() && !(CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock()) || blockState.isOf(Exploring.POINTED_BONE));
        }
    }

    @Nullable
    private static BlockPos moveDownToAir(WorldAccess world, BlockPos.Mutable mutablePos, int distance) {
        while (mutablePos.getY() > world.getBottomY() + 1) {
            BlockState blockState = world.getBlockState(mutablePos);
            if (CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock()) || blockState.isOf(Exploring.POINTED_BONE)) {
                return null;
            }

            if (blockState.isAir()) {
                return mutablePos;
            }

            mutablePos.move(Direction.DOWN);
        }

        return null;
    }

    private static boolean isAirOrLavaOcean(WorldAccess world, int seaLevel, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir() || blockState.isOf(Blocks.LAVA) && pos.getY() <= seaLevel;
    }
}
