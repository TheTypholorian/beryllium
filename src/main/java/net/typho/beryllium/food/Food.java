package net.typho.beryllium.food;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.typho.beryllium.Beryllium;

public class Food implements ModInitializer {
    public static final Item CROISSANT = Beryllium.FOOD_CONSTRUCTOR.item("croissant", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final Item BAGUETTE = Beryllium.FOOD_CONSTRUCTOR.item("baguette", new BaguetteItem(new Item.Settings().food(Beryllium.FOOD_CONSTRUCTOR.timedFood(2.5f).nutrition(9).saturationModifier(0.3F).build())));

    @Override
    public void onInitialize() {
    }
}
