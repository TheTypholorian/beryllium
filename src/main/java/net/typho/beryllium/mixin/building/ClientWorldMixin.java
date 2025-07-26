package net.typho.beryllium.mixin.building;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.typho.beryllium.building.BlockBreakingProgress;
import net.typho.beryllium.building.BlockBreakingProgressWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    @Shadow @Final private WorldRenderer worldRenderer;

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(
            method = "setBlockBreakingInfo",
            at = @At("HEAD")
    )
    private void setBlockBreakingInfo(int entityId, BlockPos pos, int progress, CallbackInfo ci) {
        BlockBreakingProgressWorld world = (BlockBreakingProgressWorld) this;

        world.blockBreakingProgress().computeIfAbsent(pos, k -> new BlockBreakingProgress(getBlockState(k), k)).progress = progress;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        BlockBreakingProgressWorld world = (BlockBreakingProgressWorld) this;

        world.blockBreakingProgress().values().removeIf(bbp -> {
            BlockState target = getBlockState(bbp.pos);

            if (!target.isOf(target.getBlock())) {
                return true;
            }

            bbp.progress -= 0.05f;

            worldRenderer.setBlockBreakingInfo(0, bbp.pos, (int) bbp.progress);

            return bbp.progress <= 0;
        });
    }
}
