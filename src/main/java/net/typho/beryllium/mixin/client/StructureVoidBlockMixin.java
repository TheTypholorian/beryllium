package net.typho.beryllium.mixin.client;

import net.minecraft.block.StructureVoidBlock;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureVoidBlock.class)
public class StructureVoidBlockMixin {
    @Shadow
    @Final
    @Mutable
    private static VoxelShape SHAPE;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void clinit(CallbackInfo ci) {
        SHAPE = VoxelShapes.fullCube();
    }
}
