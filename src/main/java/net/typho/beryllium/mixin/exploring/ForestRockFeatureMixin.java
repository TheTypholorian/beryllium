package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.ForestRockFeature;
import net.typho.beryllium.exploring.Exploring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ForestRockFeature.class)
public class ForestRockFeatureMixin {
    @WrapOperation(
            method = "generate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/ForestRockFeature;isStone(Lnet/minecraft/block/BlockState;)Z"
            )
    )
    private boolean isStone(BlockState blockState, Operation<Boolean> original) {
        return blockState.isIn(Exploring.CHORUS_PLANTABLE) || original.call(blockState);
    }
}
