package net.typho.beryllium.mixin.exploring;

import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.impl.client.render.rendertype.DynamicRenderTypeManager;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.exploring.Exploring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = DynamicRenderTypeManager.class, remap = false)
public class DynamicRenderTypeManagerMixin {
    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At("TAIL")
    )
    private void apply(Map<Identifier, byte[]> fileData, ResourceManager resourceManager, Profiler profilerFiller, CallbackInfo ci) {
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.CONGEALED_VOID, VeilRenderType.get(Beryllium.BASE_CONSTRUCTOR.id("congealed_void")));
    }
}
