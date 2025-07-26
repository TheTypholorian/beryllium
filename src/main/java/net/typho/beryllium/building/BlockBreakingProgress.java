package net.typho.beryllium.building;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class BlockBreakingProgress {
    public float progress = 0;
    public final BlockState state;
    public final BlockPos pos;

    public BlockBreakingProgress(BlockState state, BlockPos pos) {
        this.state = state;
        this.pos = pos;
    }

    public BlockBreakingProgress(BlockState state, BlockPos pos, float progress) {
        this(state, pos);
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "BlockBreakingProgress{" +
                "progress=" + progress +
                ", state=" + state +
                ", pos=" + pos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BlockBreakingProgress that = (BlockBreakingProgress) o;
        return Float.compare(progress, that.progress) == 0 && Objects.equals(state, that.state) && Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(progress, state, pos);
    }
}
