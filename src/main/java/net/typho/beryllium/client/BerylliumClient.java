package net.typho.beryllium.client;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.config.SyncServerConfigS2C;

public class BerylliumClient implements ClientModInitializer {
    public static final ClientConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncServerConfigS2C.ID, (payload, context) -> Beryllium.SERVER_CONFIG = payload.config());
    }

    public static class ClientConfig extends Config {
        public boolean hudItemTooltips = true;
        public boolean compassCoords = true;

        public ClientConfig() {
            super(Identifier.of(Beryllium.MOD_ID, "client"));
        }
    }
}
