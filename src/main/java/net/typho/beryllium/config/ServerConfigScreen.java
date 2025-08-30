package net.typho.beryllium.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;

public class ServerConfigScreen extends Screen {
    private final List<FeatureGroup> history = new LinkedList<>();
    private FeatureGroup tab = ServerConfig.BASE_GROUP;

    public ServerConfigScreen(Text title) {
        super(title);
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }

    @Override
    protected void init() {
        int width = Math.min(256, this.width / 2);
        int height = 24;
        int x = (this.width - width) / 2;
        int y = this.height / 2 - 256;

        for (FeatureGroupChild child : tab) {
            addDrawableChild(new Node(x, y, width, height, child));
            y += 48;
        }
    }

    public FeatureGroup getTab() {
        return tab;
    }

    public void pushTab(FeatureGroup tab) {
        if (this.tab != null) {
            history.add(this.tab);
        }

        setTab(tab);
    }

    public boolean popTab() {
        if (!history.isEmpty()) {
            setTab(history.removeLast());
            return true;
        }

        return false;
    }

    public void setTab(FeatureGroup tab) {
        this.tab = tab;

        clearAndInit();
    }

    @Override
    public void close() {
        if (!popTab()) {
            super.close();
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public class Node extends ClickableWidget {
        public final FeatureGroupChild child;

        public Node(int x, int y, int width, int height, FeatureGroupChild child) {
            super(x, y, width, height, null);
            this.child = child;
            child.init(this);
        }

        public ServerConfigScreen parent() {
            return ServerConfigScreen.this;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            child.click(this, mouseX, mouseY);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            int itemSize = 16;

            float scale = itemSize / 16f;
            boolean hovered = isHovered();

            if (hovered) {
                scale *= 1.5f;
            }

            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.scale(scale, scale, scale);
            matrices.translate(-itemSize / 2f + (getX() + itemSize / 2f + (height - itemSize) / 2f) / scale, -itemSize / 2f + (getY() + itemSize / 2f + (height - itemSize) / 2f) / scale, 0);
            context.drawItem(child.icon(), 0, 0);
            matrices.pop();

            context.drawTextWithShadow(textRenderer, child.name(), getX() + itemSize * 2, getY() + (height - textRenderer.fontHeight) / 2, 0xFFFFFFFF);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        }
    }
}
