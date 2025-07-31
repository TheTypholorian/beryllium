package net.typho.beryllium.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldScreenOptionGrid;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class BerylliumWorldCreationTab extends GridScreenTab {
    public BerylliumWorldCreationTab(CreateWorldScreen parent, Text title) {
        super(title);

        GridWidget.Adder adder = grid.setColumnSpacing(10).setRowSpacing(8).createAdder(2);
        WorldScreenOptionGrid.Builder builder = WorldScreenOptionGrid.builder(310);
        /*
        builder.add(Text.literal("Spawn in Village"), () -> config.spawnInVillage, v -> {
                    config.spawnInVillage = v;
                    config.markDirty();
                })
                .toggleable(() -> true);
         */
        WorldScreenOptionGrid grid = builder.build(widget -> adder.add(widget, 2));
        parent.getWorldCreator().addListener(creator -> grid.refresh());
    }
}
