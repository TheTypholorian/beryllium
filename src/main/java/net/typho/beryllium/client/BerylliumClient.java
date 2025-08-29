package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.typho.beryllium.config.Feature;
import net.typho.beryllium.config.ServerConfig;
import net.typho.beryllium.config.SyncServerConfigS2C;

public class BerylliumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncServerConfigS2C.ID, (payload, context) -> {
            Feature<?> feature = ServerConfig.ALL_FEATURES.get(payload.id());
            feature.set(payload.value());
            feature.updatedClient(MinecraftClient.getInstance());
        });
    }
}
