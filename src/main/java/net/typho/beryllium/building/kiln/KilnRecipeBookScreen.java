package net.typho.beryllium.building.kiln;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class KilnRecipeBookScreen extends AbstractFurnaceRecipeBookScreen {
    private static final Text TOGGLE_FIREABLE_RECIPES_TEXT = Text.translatable("gui.beryllium.recipebook.toggleRecipes.building.fireable");

    @Override
    protected Text getToggleCraftableButtonText() {
        return TOGGLE_FIREABLE_RECIPES_TEXT;
    }

    @Override
    protected Set<Item> getAllowedFuels() {
        return AbstractFurnaceBlockEntity.createFuelTimeMap().keySet();
    }
}
