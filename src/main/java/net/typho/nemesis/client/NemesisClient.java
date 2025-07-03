package net.typho.nemesis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;
import net.typho.nemesis.Nemesis;

public class NemesisClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Nemesis.DIAMOND_ARROW_TYPE, ctx -> new ArrowEntityRenderer(ctx) {
            @Override
            public Identifier getTexture(ArrowEntity entity) {
                return new Identifier(Nemesis.MOD_ID, "textures/entity/projectiles/diamond_arrow.png");
            }
        });
    }
}
