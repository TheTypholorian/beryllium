package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndSpikeFeature.class)
public class EndSpikeFeatureMixin {
    @ModifyArg(
            method = "generateSpike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/EndSpikeFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
                    ordinal = 0
            ),
            index = 2
    )
    private BlockState getBlock(BlockState block, @Local BlockPos pos, @Local(argsOnly = true) Random random) {
        if (pos.getY() <= 72) {
            if (random.nextInt(pos.getY() / 8 + 8) == 0) {
                return Blocks.CRYING_OBSIDIAN.getDefaultState();
            }
        }

        return block;
    }
}
