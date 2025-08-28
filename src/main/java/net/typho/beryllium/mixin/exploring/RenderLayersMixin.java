package net.typho.beryllium.mixin.exploring;

import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.exploring.CongealedVoidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {
    @Inject(
            method = "getBlockLayer",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getBlockLayer(BlockState state, CallbackInfoReturnable<RenderLayer> cir) {
        if (state.getBlock() instanceof CongealedVoidBlock) {
            cir.setReturnValue(VeilRenderType.get(Beryllium.BASE_CONSTRUCTOR.id("congealed_void")));
        }
    }
}
