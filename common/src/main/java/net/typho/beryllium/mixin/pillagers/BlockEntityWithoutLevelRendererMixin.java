package net.typho.beryllium.mixin.pillagers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.BerylliumClient;
import net.typho.beryllium.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Inject(
            method = "renderByItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void render(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay,
            CallbackInfo ci
    ) {
        if (stack.is(ModItems.ravagerShield.get())) {
            poseStack.pushPose();
            poseStack.scale(1.0F, -1.0F, -1.0F);
            Material material = BerylliumClient.ravagerShieldSprite;
            VertexConsumer consumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(
                    buffer,
                    BerylliumClient.ravagerShieldModel.renderType(material.atlasLocation()),
                    true,
                    stack.hasFoil()
            ));
            BerylliumClient.ravagerShieldModel.handle().render(poseStack, consumer, packedLight, packedOverlay);
            BerylliumClient.ravagerShieldModel.plate().render(poseStack, consumer, packedLight, packedOverlay);
            poseStack.popPose();
            ci.cancel();
        }
    }
}
