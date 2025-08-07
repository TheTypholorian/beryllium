package net.typho.beryllium.food;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.typho.beryllium.util.DualModelItem;

public class BaguetteItem extends Item implements DualModelItem {
    public BaguetteItem(Settings settings) {
        super(settings);
    }

    @Override
    public Identifier worldModel() {
        return Food.CONSTRUCTOR.id("baguette_long");
    }

    @Override
    public Identifier guiModel() {
        return Food.CONSTRUCTOR.id("baguette");
    }
}
