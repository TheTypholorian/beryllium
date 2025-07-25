package net.typho.beryllium.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.typho.beryllium.combat.EndCrystalProjectileEntity;
import org.joml.Quaternionf;

public class EndCrystalProjectileEntityRenderer extends EntityRenderer<EndCrystalProjectileEntity> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/end_crystal/end_crystal.png");
    private static final RenderLayer END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private static final float SINE_45_DEGREES = (float) Math.sin(Math.PI / 4);
    private static final String GLASS = "glass";
    private final ModelPart core;
    private final ModelPart frame;

    protected EndCrystalProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        ModelPart modelPart = context.getPart(EntityModelLayers.END_CRYSTAL);
        this.frame = modelPart.getChild(GLASS);
        this.core = modelPart.getChild(EntityModelPartNames.CUBE);
    }

    @Override
    public void render(EndCrystalProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        float j = (entity.age + tickDelta) * 3.0F;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(END_CRYSTAL);
        matrices.push();
        matrices.scale(2.0F, 2.0F, 2.0F);
        matrices.translate(0.0F, -0.5F, 0.0F);
        int k = OverlayTexture.DEFAULT_UV;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        matrices.translate(0.0F, 1F, 0.0F);
        matrices.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
        this.frame.render(matrices, vertexConsumer, light, k);
        float l = 0.875F;
        matrices.scale(l, l, l);
        matrices.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        this.frame.render(matrices, vertexConsumer, light, k);
        matrices.scale(l, l, l);
        matrices.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        this.core.render(matrices, vertexConsumer, light, k);
        matrices.pop();
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(EndCrystalProjectileEntity entity) {
        return TEXTURE;
    }
}
