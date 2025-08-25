package net.typho.beryllium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @WrapOperation(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/SkinTextures;capeTexture()Lnet/minecraft/util/Identifier;"
            )
    )
    private Identifier capeTexture(SkinTextures instance, Operation<Identifier> original, @Local(argsOnly = true) AbstractClientPlayerEntity entity) {
        if (entity.getUuidAsString().equalsIgnoreCase("eb612c97-1f52-4113-b5f6-5fc0d15b5e5b")) {
            return Beryllium.BASE_CONSTRUCTOR.id("textures/capes/ace_cape.png");
        }

        return original.call(instance);
    }
}
