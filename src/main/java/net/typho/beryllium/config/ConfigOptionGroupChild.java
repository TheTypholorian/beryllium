package net.typho.beryllium.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface ConfigOptionGroupChild {
    void add(ConfigOptionGroup group);

    Text name();

    ItemStack icon();

    default void init(ServerConfigScreen.Node node) {
    }

    default void click(ServerConfigScreen.Node node, double mouseX, double mouseY) {
    }

    default boolean scroll(ServerConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return false;
    }

    default void render(ServerConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
    }
}
