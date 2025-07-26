package net.typho.beryllium.building;

import net.minecraft.util.math.BlockPos;

import java.util.Map;

public interface BlockBreakingProgressWorld {
    Map<BlockPos, BlockBreakingProgress> blockBreakingProgress();
}
