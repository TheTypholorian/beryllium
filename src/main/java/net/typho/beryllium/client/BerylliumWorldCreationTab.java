package net.typho.beryllium.client;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldScreenOptionGrid;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.config.Property;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BerylliumWorldCreationTab extends GridScreenTab {
    public BerylliumWorldCreationTab(CreateWorldScreen parent, Text title) {
        super(title);

        //ElementListWidget<?> list = new ElementListWidget<ElementListWidget.Entry<>>() {
        //};

        GridWidget.Adder adder = grid.setColumnSpacing(10).setRowSpacing(8).createAdder(2);
        WorldScreenOptionGrid.Builder builder = WorldScreenOptionGrid.builder(310);
        int i = 0;

        for (Property<?> property : Beryllium.SERVER_CONFIG.properties.values()) {
            property.addWidget(adder, i++);
        }

        WorldScreenOptionGrid grid = builder.build(widget -> adder.add(widget, 2));
        parent.getWorldCreator().addListener(creator -> grid.refresh());
    }

    public static class Entry extends ElementListWidget.Entry<Entry> {
        public final TextWidget text;
        public final ClickableWidget button;

        public Entry(TextWidget text, ClickableWidget button) {
            this.text = text;
            this.button = button;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(text, button);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(text, button);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        }
    }

    public static class ListWidget extends ElementListWidget<Entry> {
        public ListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l) {
            super(minecraftClient, i, j, k, l);
        }
    }
}
