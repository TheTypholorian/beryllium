package net.typho.beryllium.client;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.config.Config;
import net.typho.beryllium.config.SyncServerConfigS2C;

public class BerylliumClient implements ClientModInitializer {
    public static final ClientConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncServerConfigS2C.ID, (payload, context) -> Config.properties.get(payload.id()).set(payload.value()));
    }

    public static class ClientConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
        public boolean hudItemTooltips = true;
        public boolean compassCoords = true;

        public ClientConfig() {
            super(Identifier.of(Beryllium.MOD_ID, "client"));
        }
    }
}
