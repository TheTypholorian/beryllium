package net.typho.beryllium.mixin.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends PlantBlock {
    @Shadow
    @Final
    public static IntProperty AGE;

    protected CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return Beryllium.SERVER_CONFIG.cropComparatorOutput.get();
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.getOrEmpty(AGE).orElse(-1) + 1;
    }

    @ModifyConstant(
            method = {"randomTick", "applyGrowth"},
            constant = @Constant(intValue = Block.NOTIFY_LISTENERS)
    )
    private static int flags(int flags) {
        return Block.NOTIFY_ALL;
    }
}
