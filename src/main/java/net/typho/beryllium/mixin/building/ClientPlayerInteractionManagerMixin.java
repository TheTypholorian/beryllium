package net.typho.beryllium.mixin.building;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.typho.beryllium.building.BlockBreakingProgress;
import net.typho.beryllium.building.BlockBreakingProgressWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow
    private float currentBreakingProgress;

    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(
            method = "cancelBlockBreaking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/world/ClientWorld;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V"
            )
    )
    private void setBlockBreakingInfo(ClientWorld instance, int entityId, BlockPos pos, int progress) {
    }

    @Inject(
            method = "attackBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;F)V",
                    shift = At.Shift.AFTER
            )
    )
    private void currentBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        assert client.world != null;
        currentBreakingProgress = ((BlockBreakingProgressWorld) client.world).blockBreakingProgress().computeIfAbsent(pos, k -> new BlockBreakingProgress(client.world.getBlockState(k), k, 0)).progress;
    }
}
