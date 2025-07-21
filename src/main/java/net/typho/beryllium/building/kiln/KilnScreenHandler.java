package net.typho.beryllium.building.kiln;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.typho.beryllium.Beryllium;

public class KilnScreenHandler extends AbstractFurnaceScreenHandler {
    public KilnScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Beryllium.BUILDING.KILN_SCREEN_HANDLER_TYPE, Beryllium.BUILDING.KILN_RECIPE_TYPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public KilnScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Beryllium.BUILDING.KILN_SCREEN_HANDLER_TYPE, Beryllium.BUILDING.KILN_RECIPE_TYPE, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
