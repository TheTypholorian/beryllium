package net.typho.beryllium.mixin.combat;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.CombatComponents;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntityRenderer.class)
public abstract class TridentEntityRendererMixin extends EntityRenderer<TridentEntity> {
    protected TridentEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(TridentEntity trident, Frustum frustum, double x, double y, double z) {
        if (super.shouldRender(trident, frustum, x, y, z)) {
            return true;
        }

        return EnchantmentHelper.getLevel(
                trident.getWorld()
                        .getRegistryManager()
                        .get(RegistryKeys.ENCHANTMENT)
                        .getEntry(Beryllium.COMBAT.id("reeling"))
                        .orElseThrow(),
                trident.getItemStack()
        ) > 0;
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/projectile/TridentEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("TAIL")
    )
    private void render(TridentEntity trident, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (trident.getItemStack() != null && trident.getOwner() != null) {
            if (trident.getComponent(CombatComponents.REELING).getReeling() > 0) {
                matrices.push();
                Vec3d vec3d = trident.getOwner().getLeashPos(tickDelta);
                double d = trident.lerpYaw(tickDelta) * (float) (Math.PI / 180.0) + (Math.PI / 2);
                Vec3d vec3d2 = trident.getLeashOffset(tickDelta);
                double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
                double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
                double g = MathHelper.lerp(tickDelta, trident.prevX, trident.getX()) + e;
                double h = MathHelper.lerp(tickDelta, trident.prevY, trident.getY()) + vec3d2.y;
                double i = MathHelper.lerp(tickDelta, trident.prevZ, trident.getZ()) + f;
                matrices.translate(e, vec3d2.y, f);
                float j = (float)(vec3d.x - g);
                float k = (float)(vec3d.y - h);
                float l = (float)(vec3d.z - i);
                float m = 0.025F;
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float n = MathHelper.inverseSqrt(j * j + l * l) * m / 2.0F;
                float o = l * n;
                float p = j * n;

                for (int u = 0; u <= 24; u++) {
                    renderChainSegment(vertexConsumer, matrix4f, j, k, l, m, m, o, p, u, false, light);
                }

                for (int u = 24; u >= 0; u--) {
                    renderChainSegment(vertexConsumer, matrix4f, j, k, l, m, 0.0F, o, p, u, true, light);
                }

                matrices.pop();
            }
        }
    }

    @Unique
    private static void renderChainSegment(
            VertexConsumer vertexConsumer,
            Matrix4f matrix,
            float leashedEntityX,
            float leashedEntityY,
            float leashedEntityZ,
            float f,
            float g,
            float h,
            float i,
            int segmentIndex,
            boolean isLeashKnot,
            int light
    ) {
        float j = segmentIndex / 24.0F;
        float red, green, blue;

        if (segmentIndex % 2 == (isLeashKnot ? 1 : 0)) {
            red = 73 / 255f;
            green = 80 / 255f;
            blue = 101 / 255f;
        } else {
            red = 37 / 255f;
            green = 44 / 255f;
            blue = 61 / 255f;
        }

        float r = leashedEntityX * j;
        float s = leashedEntityY > 0.0F ? leashedEntityY * j * j : leashedEntityY - leashedEntityY * (1.0F - j) * (1.0F - j);
        float t = leashedEntityZ * j;
        vertexConsumer.vertex(matrix, r - h, s + g, t + i).color(red, green, blue, 1.0F).light(light);
        vertexConsumer.vertex(matrix, r + h, s + f - g, t - i).color(red, green, blue, 1.0F).light(light);
    }
}
