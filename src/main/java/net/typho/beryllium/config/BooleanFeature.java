package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class BooleanFeature extends Feature<Boolean> {
    public final ItemStack icon;

    public BooleanFeature(ItemStack icon, FeatureGroup parent, String name, Boolean value) {
        super(parent, name, BoolArgumentType.bool(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen.Node node, double mouseX, double mouseY) {
        setUpdateSendClient(!get());
    }

    @Override
    public boolean scroll(ServerConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        click(node, mouseX, mouseY);
        return true;
    }

    @Override
    public void render(ServerConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = get() ? ScreenTexts.ON : ScreenTexts.OFF;
        context.drawTextWithShadow(node.textRenderer(), text, node.getX() + node.getWidth() - node.textRenderer().getWidth(text) - 8, node.getY() + (node.getHeight() - node.textRenderer().fontHeight) / 2, 0xFFFFFFFF);
    }

    @Override
    public Codec<Boolean> codec() {
        return Codec.BOOL;
    }

    @Override
    public PacketCodec<ByteBuf, Boolean> packetCodec() {
        return PacketCodecs.BOOL;
    }
}
