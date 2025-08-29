package net.typho.beryllium.mixin.redstone;

import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    /**
     * @author The Typhothanian
     * @reason Makes tnt instantly explode when chain exploded
     */
    @Inject(
            method = "onDestroyedByExplosion",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, CallbackInfo ci) {
        if (ServerConfig.INSTANT_CHAIN_TNT.get()) {
            if (!world.isClient) {
                world.createExplosion(
                        null,
                        Explosion.createDamageSource(world, null),
                        null,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        4,
                        false,
                        World.ExplosionSourceType.TNT
                );
            }

            ci.cancel();
        }
    }
}
