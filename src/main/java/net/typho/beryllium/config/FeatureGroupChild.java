package net.typho.beryllium.config;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface FeatureGroupChild {
    void add(FeatureGroup group);

    Text name();

    ItemStack icon();

    void click(ServerConfigScreen screen);
}
