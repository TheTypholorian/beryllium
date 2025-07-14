package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.*;
import org.joml.Quaternionf;

import java.util.List;

public class BerylliumClient implements ClientModInitializer {
    public static class EndCrystalProjectileEntityRenderer extends EntityRenderer<EndCrystalProjectileEntity> {
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

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Combat.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(DiamondArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/diamond_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.IRON_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(IronArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/iron_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.FLAMING_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(FlamingArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/flaming_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.COPPER_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(CopperArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/copper_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.END_CRYSTAL_PROJECTILE_ENTITY, EndCrystalProjectileEntityRenderer::new);

        HudRenderCallback.EVENT.register((context, renderTickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            ItemStack main = client.player.getMainHandStack();

            if (!main.isEmpty()) {
                drawMainTooltip(context, main, client.getWindow().getScaledWidth() / 2 - 127 - 4, client.getWindow().getScaledHeight() - 3, client.player, client.textRenderer);
            }

            ItemStack off = client.player.getOffHandStack();

            if (!off.isEmpty()) {
                drawOffTooltip(context, off, client.getWindow().getScaledWidth() / 2 + 127 + 4, client.getWindow().getScaledHeight() - 3, client.player, client.textRenderer);
            }
        });
    }

    public static void drawMainTooltip(DrawContext context, ItemStack stack, int x, int y, PlayerEntity player, TextRenderer renderer) {
        List<Text> tooltip = stack.getTooltip(Item.TooltipContext.DEFAULT, player, TooltipType.BASIC);
        List<OrderedText> lines = tooltip.stream()
                .flatMap(t -> renderer.wrapLines(t, 160).stream())
                .toList();
        int width = lines.stream().mapToInt(renderer::getWidth).max().orElse(0) + 8;
        int height = lines.size() * renderer.fontHeight + 8;

        if (player.getMainArm() == Arm.RIGHT) {
            x -= width;
        }

        y -= height;

        context.fill(x, y, x + width, y + height - 1, 0xF0100010);

        for (int i = 0; i < lines.size(); i++) {
            context.drawTextWithShadow(renderer, lines.get(i), x + 4, y + 4 + i * renderer.fontHeight, 0xFFFFFFFF);
        }
    }

    public static void drawOffTooltip(DrawContext context, ItemStack stack, int x, int y, PlayerEntity player, TextRenderer renderer) {
        List<Text> tooltip = stack.getTooltip(Item.TooltipContext.DEFAULT, player, TooltipType.BASIC);
        List<OrderedText> lines = tooltip.stream()
                .flatMap(t -> renderer.wrapLines(t, 160).stream())
                .toList();
        int width = lines.stream().mapToInt(renderer::getWidth).max().orElse(0) + 8;
        int height = lines.size() * renderer.fontHeight + 8;

        if (player.getMainArm() == Arm.LEFT) {
            x -= width;
        }

        y -= height;

        context.fill(x, y, x + width, y + height - 1, 0xF0100010);

        for (int i = 0; i < lines.size(); i++) {
            context.drawTextWithShadow(renderer, lines.get(i), x + 4, y + 4 + i * renderer.fontHeight, 0xFFFFFFFF);
        }
    }
}
