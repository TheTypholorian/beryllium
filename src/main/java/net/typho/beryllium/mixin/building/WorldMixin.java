package net.typho.beryllium.mixin.building;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.typho.beryllium.building.BlockBreakingProgress;
import net.typho.beryllium.building.BlockBreakingProgressWorld;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(World.class)
@Implements(@Interface(iface = BlockBreakingProgressWorld.class, prefix = "bbp$"))
public class WorldMixin {
    @Unique
    public final Map<BlockPos, BlockBreakingProgress> blockBreakingProgress = new LinkedHashMap<>();

    @Unique
    public Map<BlockPos, BlockBreakingProgress> bbp$blockBreakingProgress() {
        return blockBreakingProgress;
    }
}
