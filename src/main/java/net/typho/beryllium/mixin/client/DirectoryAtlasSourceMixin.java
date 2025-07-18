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
    private void method_47683(ResourceFinder finder, AtlasSource.SpriteRegions regions, Identifier identifier, Resource resource, CallbackInfo ci) {
        identifier = finder.toResourceId(identifier).withPrefixedPath(prefix);
        DynamicSpriteLoader loader = BerylliumClient.DYNAMIC_TEXTURES.get(identifier);

        if (loader != null) {
            loader.loadSprite(finder, regions, identifier, resource);
            ci.cancel();
        }

        /*
        if (Objects.equals(identifier.getNamespace(), Beryllium.MOD_ID) && Objects.equals(identifier.getPath(), "textures/item/metal_detector.png")) {
            regions.add(resourceFinder.toResourceId(identifier).withPrefixedPath(prefix), opener -> {
                try {
                    Identifier target = resourceFinder.toResourceId(identifier).withPrefixedPath(prefix);
                    NativeImage nativeImage = new NativeImage(16, 16, false);
                    NativeImage compass = NativeImage.read(MinecraftClient.getInstance().getResourceManager().getResource(Identifier.ofVanilla("textures/item/compass_00.png")).orElseThrow().getInputStream());

                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            int color = compass.getColor(x, y);

                            color = switch (color) {
                                case 0xFFFFFFFF -> 0xFFB6C3FB;
                                case 0xFFD8D8D8 -> 0xFF8299FC;
                                case 0xFFA8A8A8 -> 0xFF567CE7;
                                case 0xFF828282 -> 0xFF365AC1;
                                case 0xFF5E5E5E, 0xFF646464 -> 0xFF314E9C;
                                case 0xFF353535 -> 0xFF29418A;
                                case 0xFF2F2F2F, 0xFF171718, 0xFF4D4D4F -> 0xFF21346D;
                                default -> color;
                            };

                            nativeImage.setColor(x, y, color);
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
         */
    }
}
