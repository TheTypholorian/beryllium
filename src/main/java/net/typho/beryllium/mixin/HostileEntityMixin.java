package net.typho.beryllium.mixin;

import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
    @Inject(
            method = "isSpawnDark",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void isSpawnDark(ServerWorldAccess world, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (Beryllium.SERVER_CONFIG.ultraDark.get()) {
            cir.setReturnValue(world.getLightLevel(LightType.BLOCK, pos) > 0);
        }
    }
}
