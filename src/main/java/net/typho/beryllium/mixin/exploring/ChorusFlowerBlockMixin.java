package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.typho.beryllium.exploring.BlackOpalOreBlock;
import net.typho.beryllium.exploring.Exploring;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {
    @Shadow
    @Final
    private Block plantBlock;

    @Inject(
            method = "grow",
            at = @At("HEAD")
    )
    private void grow(World world, BlockPos pos, int age, CallbackInfo ci) {
        BlockState below = world.getBlockState(pos = pos.down().down());

        if (below.getBlock() instanceof BlackOpalOreBlock) {
            int stage = below.get(BlackOpalOreBlock.STAGE);

            if (stage < 3) {
                world.setBlockState(pos, below.with(BlackOpalOreBlock.STAGE, stage + 1));
            }
        } else if (below.isOf(Blocks.END_STONE)) {
            world.setBlockState(pos, Exploring.ONYX_ORE.getDefaultState());
        }
    }

    @WrapOperation(
            method = {
                    "randomTick",
                    "canPlaceAt"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean isOf(BlockState instance, Block block, Operation<Boolean> original) {
        if (block != Blocks.END_STONE) {
            return original.call(instance, block);
        }

        return original.call(instance, block) || instance.getBlock() instanceof BlackOpalOreBlock;
    }
}
