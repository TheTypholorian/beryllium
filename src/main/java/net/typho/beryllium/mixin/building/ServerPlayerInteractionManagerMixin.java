package net.typho.beryllium.mixin.building;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Redirect(
            method = "processBlockBreakingAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V"
            )
    )
    private void setBlockBreakingInfo(ServerWorld instance, int entityId, BlockPos pos, int progress) {
        if (progress != -1) {
            instance.setBlockBreakingInfo(entityId, pos, progress);
        }
    }
}
