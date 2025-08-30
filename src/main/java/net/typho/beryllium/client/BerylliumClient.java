package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.typho.beryllium.config.SetConfigValuePacket;

public class BerylliumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SetConfigValuePacket.ID, (payload, context) -> {
            payload.feature().set(payload.value());
            payload.feature().updatedClient(MinecraftClient.getInstance());
        });
    }
}
