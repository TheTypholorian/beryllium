package net.typho.nemesis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.typho.nemesis.Nemesis;
import org.joml.Quaternionf;

public class NemesisClient implements ClientModInitializer {
    public static class EndCrystalProjectileEntityRenderer extends EntityRenderer<Nemesis.EndCrystalProjectileEntity> {
        private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/end_crystal/end_crystal.png");
        private static final RenderLayer END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(TEXTURE);
        private static final float SINE_45_DEGREES = (float)Math.sin(Math.PI / 4);
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
        public void render(Nemesis.EndCrystalProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
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
        public Identifier getTexture(Nemesis.EndCrystalProjectileEntity entity) {
            return TEXTURE;
        }
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Nemesis.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(Nemesis.DiamondArrowEntity entity) {
                return Identifier.of(Nemesis.MOD_ID, "textures/entity/projectiles/diamond_arrow.png");
            }
        });
        EntityRendererRegistry.register(Nemesis.IRON_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(Nemesis.IronArrowEntity entity) {
                return Identifier.of(Nemesis.MOD_ID, "textures/entity/projectiles/iron_arrow.png");
            }
        });
        EntityRendererRegistry.register(Nemesis.FLAMING_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(Nemesis.FlamingArrowEntity entity) {
                return Identifier.of(Nemesis.MOD_ID, "textures/entity/projectiles/flaming_arrow.png");
            }
        });
        EntityRendererRegistry.register(Nemesis.SHOCK_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(Nemesis.ShockArrowEntity entity) {
                return Identifier.of(Nemesis.MOD_ID, "textures/entity/projectiles/shock_arrow.png");
            }
        });
        EntityRendererRegistry.register(Nemesis.END_CRYSTAL_PROJECTILE_ENTITY, EndCrystalProjectileEntityRenderer::new);
    }
}
