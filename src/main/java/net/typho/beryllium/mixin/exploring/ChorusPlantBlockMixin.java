package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusPlantBlock;
import net.typho.beryllium.exploring.Exploring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin {
    @WrapOperation(
            method = {
                    "withConnectionProperties",
                    "getStateForNeighborUpdate",
                    "canPlaceAt"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private static boolean isOf(BlockState instance, Block block, Operation<Boolean> original) {
        if (block != Blocks.END_STONE) {
            return original.call(instance, block);
        }

        return original.call(instance, block) || instance.isIn(Exploring.CHORUS_PLANTABLE);
    }
}
