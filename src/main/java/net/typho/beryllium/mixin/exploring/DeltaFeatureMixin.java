package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.DeltaFeature;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DeltaFeature.class)
public class DeltaFeatureMixin {
    @WrapOperation(
            method = "canPlace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isAir()Z"
            )
    )
    private static boolean canPlace(BlockState instance, Operation<Boolean> original) {
        return original.call(instance) || instance.isOf(Beryllium.EXPLORING.POINTED_BONE);
    }
}
