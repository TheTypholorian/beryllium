package net.typho.beryllium.exploring;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.typho.beryllium.Beryllium;

public class RiverAlgaeFeature extends Feature<DefaultFeatureConfig> {
    public RiverAlgaeFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        boolean generated = false;
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int x = random.nextInt(8) - random.nextInt(8);
        int z = random.nextInt(8) - random.nextInt(8);
        int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin.getX() + x, origin.getZ() + z);
        BlockPos place = new BlockPos(origin.getX() + x, y, origin.getZ() + z);

        if (world.getBlockState(place).isOf(Blocks.WATER)) {
            BlockState placeState = AlgaeBlock.createFullState(Beryllium.EXPLORING.ALGAE_BLOCK.getDefaultState(), world, place).with(Properties.WATERLOGGED, true);

            if (placeState.canPlaceAt(world, place)) {
                world.setBlockState(place, placeState, Block.NOTIFY_LISTENERS);

                generated = true;
            }
        }

        return generated;
    }
}
