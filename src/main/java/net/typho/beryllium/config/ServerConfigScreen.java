package net.typho.beryllium.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;

public class ServerConfigScreen extends Screen {
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private FeatureGroup tab = ServerConfig.ENCHANTING_GROUP;

    protected ServerConfigScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        layout.addHeader(Text.literal("header"), textRenderer);
    }
}
