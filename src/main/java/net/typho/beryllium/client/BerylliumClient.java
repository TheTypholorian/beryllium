package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.typho.beryllium.config.Property;
import net.typho.beryllium.config.ServerConfig;
import net.typho.beryllium.config.SyncServerConfigS2C;

public class BerylliumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncServerConfigS2C.ID, (payload, context) -> {
            Property<?> property = ServerConfig.properties.get(payload.id());
            property.set(payload.value());
            property.updatedClient(MinecraftClient.getInstance());
        });
    }
}
