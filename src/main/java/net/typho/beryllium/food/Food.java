package net.typho.beryllium.food;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.typho.beryllium.Module;

public class Food extends Module {
    public final Block LETTUCE = blockWithItem("lettuce", new CropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT)), new Item.Settings());

    public final Item CROISSANT = item("croissant", new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.6F).build())));

    public Food(String name) {
        super(name);
    }

    @Override
    public void onInitialize() {
    }
}
