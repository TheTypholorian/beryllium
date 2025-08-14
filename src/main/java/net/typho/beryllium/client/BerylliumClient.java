package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.typho.beryllium.config.Config;
import net.typho.beryllium.config.SyncServerConfigS2C;

public class BerylliumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncServerConfigS2C.ID, (payload, context) -> Config.properties.get(payload.id()).set(payload.value()));
    }
}
