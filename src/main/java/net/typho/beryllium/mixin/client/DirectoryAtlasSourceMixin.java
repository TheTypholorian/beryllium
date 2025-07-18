package net.typho.beryllium.mixin.client;

import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.DirectoryAtlasSource;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.util.Identifier;
import net.typho.beryllium.client.BerylliumClient;
import net.typho.beryllium.client.DynamicSpriteLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private void method_47683(ResourceFinder finder, AtlasSource.SpriteRegions regions, Identifier file, Resource resource, CallbackInfo ci) {
        DynamicSpriteLoader loader = BerylliumClient.DYNAMIC_TEXTURES.get(file);

        if (loader != null) {
            loader.loadSprite(finder, regions, file, resource);
            ci.cancel();
        }

        /*
        if (prefix.contains("block") || prefix.contains("item") || prefix.contains("entity")) {
            Identifier target = finder.toResourceId(file).withPrefixedPath(prefix);
            regions.add(target, opener -> {
                try {
                    NativeImage original = NativeImage.read(MinecraftClient.getInstance().getResourceManager().getResource(file).orElseThrow().getInputStream());
                    NativeImage output = new NativeImage(original.getWidth(), original.getHeight(), false);

                    for (int x = 0; x < output.getWidth(); x++) {
                        for (int y = 0; y < output.getHeight(); y++) {
                            int color = original.getColor(x, y);

                            float red = (color & 0xFF) / 127f;
                            float green = (color >> 8 & 0xFF) / 127f;
                            float blue = (color >> 16 & 0xFF) / 127f;

                            red = red * red * red;
                            green = green * green * green;
                            blue = blue * blue * blue;

                            output.setColor(x, y, (color & 0xFF000000) | ((int) Math.min(255, red * 127)) | ((int) Math.min(255, green * 127) << 8) | ((int) Math.min(255, blue * 127) << 16));
                        }
                    }

                    return new SpriteContents(target, new SpriteDimensions(16, 16), output, new ResourceMetadata() {
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
         */
    }
}
