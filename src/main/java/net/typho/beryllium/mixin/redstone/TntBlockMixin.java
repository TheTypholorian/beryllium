package net.typho.beryllium.mixin.redstone;

import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    /**
     * @author The Typhothanian
     * @reason Makes tnt instantly explode when chain exploded
     */
    @Overwrite
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
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
    }
}
