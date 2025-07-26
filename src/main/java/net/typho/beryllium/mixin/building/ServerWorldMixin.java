package net.typho.beryllium.mixin.building;

import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.typho.beryllium.building.BlockBreakingProgress;
import net.typho.beryllium.building.BlockBreakingProgressWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
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

            int ip = (int) bbp.progress;

            bbp.progress -= 0.05f;

            if ((int) bbp.progress != ip) {
                for (ServerPlayerEntity serverPlayerEntity : Objects.requireNonNull(getServer()).getPlayerManager().getPlayerList()) {
                    if (serverPlayerEntity != null) {
                        double x = bbp.pos.getX() - serverPlayerEntity.getX();
                        double y = bbp.pos.getY() - serverPlayerEntity.getY();
                        double z = bbp.pos.getZ() - serverPlayerEntity.getZ();

                        if (x * x + y * y + z * z < 1024) {
                            serverPlayerEntity.networkHandler.sendPacket(new BlockBreakingProgressS2CPacket(0, bbp.pos, (int) bbp.progress));
                        }
                    }
                }
            }

            return bbp.progress <= 0;
        });
    }
}
