package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;

public class FloatConfigOption extends ConfigOption<Float> {
    public final ItemStack icon;

    public FloatConfigOption(ItemStack icon, ConfigOptionGroup parent, String name, Float value) {
        super(parent, name, FloatArgumentType.floatArg(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public boolean scroll(ServerConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        setUpdateSendClient(get() + (float) verticalAmount / 10f);
        return true;
    }

    @Override
    public void render(ServerConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = Text.literal(String.format("%.1f", get()));
        context.drawTextWithShadow(node.textRenderer(), text, node.getX() + node.getWidth() - node.textRenderer().getWidth(text) - 8, node.getY() + (node.getHeight() - node.textRenderer().fontHeight) / 2, 0xFFFFFFFF);
    }

    @Override
    public Codec<Float> codec() {
        return Codec.FLOAT;
    }

    @Override
    public PacketCodec<ByteBuf, Float> packetCodec() {
        return PacketCodecs.FLOAT;
    }
}
