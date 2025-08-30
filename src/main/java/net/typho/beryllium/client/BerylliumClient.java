package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.typho.beryllium.config.SetConfigValuePacket;

public class BerylliumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SetConfigValuePacket.ID, (payload, context) -> {
            payload.option().set(payload.value());
            payload.option().updatedClient(MinecraftClient.getInstance());
        });
    }
}
