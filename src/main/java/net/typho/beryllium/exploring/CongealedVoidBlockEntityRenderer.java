package net.typho.beryllium.exploring;

import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.typho.beryllium.Beryllium;
import org.joml.Matrix4f;

public class CongealedVoidBlockEntityRenderer implements BlockEntityRenderer<CongealedVoidBlockEntity> {
    @Override
    public void render(CongealedVoidBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        VertexConsumer consumer = vertexConsumers.getBuffer(VeilRenderType.get(Beryllium.BASE_CONSTRUCTOR.id("congealed_void")));

        renderSide(entity, matrix, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        renderSide(entity, matrix, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        renderSide(entity, matrix, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        renderSide(entity, matrix, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        renderSide(entity, matrix, consumer, 0.0F, 1.0F, 0, 0, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        renderSide(entity, matrix, consumer, 0.0F, 1.0F, 1, 1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderSide(
            CongealedVoidBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side
    ) {
        if (entity.shouldDrawSide(side)) {
            vertices.vertex(model, x1, y1, z1).color(127, 89, 108, 191);
            vertices.vertex(model, x2, y1, z2).color(127, 89, 108, 191);
            vertices.vertex(model, x2, y2, z3).color(127, 89, 108, 191);
            vertices.vertex(model, x1, y2, z4).color(127, 89, 108, 191);
        }
    }
}
