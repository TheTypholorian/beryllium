package net.typho.beryllium.goats

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Saddleable

class UnrestrictedSaddleLayer<T : Entity, M : EntityModel<T>>(
    renderer: RenderLayerParent<T, M>,
    private val model: M,
    private val textureLocation: ResourceLocation
) : RenderLayer<T, M>(renderer) {
    override fun render(
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        livingEntity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTick: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        if (livingEntity is Saddleable && livingEntity.isSaddled) {
            parentModel.copyPropertiesTo(model)
            model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick)
            model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
            val vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(textureLocation))
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY)
        }
    }
}