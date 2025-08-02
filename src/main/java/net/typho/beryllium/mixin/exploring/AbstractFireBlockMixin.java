package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.exploring.VoidFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {
    @Inject(
            method = "getState",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void getState(BlockView world, BlockPos pos, CallbackInfoReturnable<BlockState> cir, @Local BlockState blockState) {
        if (VoidFireBlock.isVoidBase(blockState)) {
            cir.setReturnValue(Exploring.VOID_FIRE.getDefaultState());
        }
    }
}
