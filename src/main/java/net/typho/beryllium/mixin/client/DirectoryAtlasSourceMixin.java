package net.typho.beryllium.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.DirectoryAtlasSource;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Mixin(DirectoryAtlasSource.class)
public class DirectoryAtlasSourceMixin {
    @Shadow
    @Final
    private String prefix;

    @Inject(
            method = "method_47683",
            at = @At("HEAD"),
            cancellable = true
    )
    private void method_47683(ResourceFinder resourceFinder, AtlasSource.SpriteRegions regions, Identifier identifier, Resource resource, CallbackInfo ci) {
        if (Objects.equals(identifier.getPath(), "textures/item/iron_sword.png")) {
            regions.add(resourceFinder.toResourceId(identifier).withPrefixedPath(prefix), opener -> {
                try {
                    Identifier target = resourceFinder.toResourceId(identifier).withPrefixedPath(prefix);
                    NativeImage nativeImage = new NativeImage(16, 16, false);
                    NativeImage copy1 = NativeImage.read(MinecraftClient.getInstance().getResourceManager().getResource(Identifier.ofVanilla("textures/item/diamond_sword.png")).orElseThrow().getInputStream());
                    NativeImage copy2 = NativeImage.read(MinecraftClient.getInstance().getResourceManager().getResource(Identifier.ofVanilla("textures/item/netherite_sword.png")).orElseThrow().getInputStream());
                    NativeImage copy3 = NativeImage.read(MinecraftClient.getInstance().getResourceManager().getResource(Identifier.ofVanilla("textures/item/golden_sword.png")).orElseThrow().getInputStream());

                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            nativeImage.setColor(x, y, copy1.getColor(x, y) | copy2.getColor(x, y) | copy3.getColor(x, y));
                        }
                    }

                    return new SpriteContents(target, new SpriteDimensions(16, 16), nativeImage, new ResourceMetadata() {
                        @Override
                        public <T> Optional<T> decode(ResourceMetadataReader<T> reader) {
                            return Optional.empty();
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            ci.cancel();
        }
    }
}
