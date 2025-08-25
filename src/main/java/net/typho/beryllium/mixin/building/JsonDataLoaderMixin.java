package net.typho.beryllium.mixin.building;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.typho.beryllium.building.Building;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

@Mixin(value = JsonDataLoader.class, priority = 100)
public class JsonDataLoaderMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    /**
     * @author The Typhothanian
     * @reason Delete vanilla stonecutting recipes
     */
    @Overwrite
    public static void load(ResourceManager manager, String dataType, Gson gson, Map<Identifier, JsonElement> results) {
        ResourceFinder resourceFinder = ResourceFinder.json(dataType);

        for (Map.Entry<Identifier, Resource> entry : resourceFinder.findResources(manager).entrySet()) {
            Identifier identifier = entry.getKey();
            Identifier identifier2 = resourceFinder.toResourceId(identifier);

            if (Building.cancelJsonResource(identifier2)) {
                continue;
            }

            try {
                Reader reader = entry.getValue().getReader();

                try {
                    JsonElement jsonElement = JsonHelper.deserialize(gson, reader, JsonElement.class);
                    JsonElement jsonElement2 = results.put(identifier2, jsonElement);
                    if (jsonElement2 != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + identifier2);
                    }
                } catch (Throwable var13) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }

                    throw var13;
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (IllegalArgumentException | IOException | JsonParseException var14) {
                LOGGER.error("Couldn't parse data file {} from {}", identifier2, identifier, var14);
            }
        }
    }
}
