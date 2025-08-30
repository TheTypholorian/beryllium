package net.typho.beryllium.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface FeatureGroupChild {
    void add(FeatureGroup group);

    Text name();

    ItemStack icon();

    default void init(ServerConfigScreen.Node node) {
    }

    default void click(ServerConfigScreen.Node node, double mouseX, double mouseY) {
    }

    default void render(ServerConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
    }
}
