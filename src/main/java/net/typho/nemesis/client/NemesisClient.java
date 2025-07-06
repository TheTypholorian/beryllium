package net.typho.nemesis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.typho.nemesis.Nemesis;

public class NemesisClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Nemesis.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(Nemesis.DiamondArrowEntity entity) {
                return new Identifier(Nemesis.MOD_ID, "textures/entity/projectiles/diamond_arrow.png");
            }
        });
    }
}
