package net.typho.beryllium.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;

public class ServerConfigScreen extends Screen {
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final List<FeatureGroup> history = new LinkedList<>();
    private FeatureGroup tab = ServerConfig.BASE_GROUP;

    public ServerConfigScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        layout.addHeader(Text.literal("header"), textRenderer);

        int y = height / 2 - 256;

        for (FeatureGroupChild child : tab) {
            addDrawableChild(new Node(width / 2 - 128, y, 256, 24, child));
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

    public class Node extends ClickableWidget {
        public final FeatureGroupChild child;

        public Node(int x, int y, int width, int height, FeatureGroupChild child) {
            super(x, y, width, height, null);
            this.child = child;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            child.click(ServerConfigScreen.this);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + width, getY() + height, 0xFF7F3FFF);

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
