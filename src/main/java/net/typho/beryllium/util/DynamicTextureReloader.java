package net.typho.beryllium.util;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.typho.beryllium.BerylliumModule;

public class DynamicTextureReloader implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return BerylliumModule.id("reload");
    }

    @Override
    public void reload(ResourceManager manager) {
        System.out.println("Reload");
        NativeImage img = new NativeImage(16, 16, false);

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                int col = ((x ^ y) & 1) == 0 ? 0xFF_00FF00 : 0xFF_0000FF;
                img.setColor(x, y, col);
            }
        }

        NativeImageBackedTexture tex = new NativeImageBackedTexture(img);

        for (int i = 0; i < 6; i++) {
            MinecraftClient.getInstance()
                    .getTextureManager()
                    .registerTexture(Identifier.of("textures/gui/title/background/panorama_" + i + ".png"), tex);
        }

        MinecraftClient.getInstance()
                .getTextureManager()
                .registerTexture(Identifier.of("textures/gui/title/minecraft.png"), tex);
        MinecraftClient.getInstance()
                .getTextureManager()
                .registerTexture(Identifier.of("textures/gui/title/edition.png"), tex);
    }
}
