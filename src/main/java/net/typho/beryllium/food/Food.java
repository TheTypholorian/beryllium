package net.typho.beryllium.food;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.typho.beryllium.Module;

public class Food extends Module {
    public final Item CROISSANT = item("croissant", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.6F).build())));

    public Food(String name) {
        super(name);
    }

    @Override
    public void onInitialize() {
    }
}
