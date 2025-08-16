package net.typho.beryllium.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public interface ClientConfig {
    SimpleOption<Boolean> hudItemTooltips();

    SimpleOption<Boolean> compassCoords();

    static ClientConfig get() {
        return (ClientConfig) MinecraftClient.getInstance().options;
    }
}
