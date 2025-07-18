package net.typho.beryllium.client;

import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.util.Identifier;

public interface DynamicSpriteLoader {
    void loadSprite(ResourceFinder finder, AtlasSource.SpriteRegions regions, Identifier id, Resource original);
}
