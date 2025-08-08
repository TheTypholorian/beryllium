package net.typho.beryllium.food;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.typho.beryllium.util.Constructor;

public class Food implements ModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("food");

    public static final Item CROISSANT = CONSTRUCTOR.item("croissant", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final Item BAGUETTE = CONSTRUCTOR.item("baguette", new BaguetteItem(new Item.Settings().food(CONSTRUCTOR.timedFood(2.5f).nutrition(9).saturationModifier(0.6F).build())));

    @Override
    public void onInitialize() {
    }
}
