package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;

public class IntFeature extends Feature<Integer> {
    public final ItemStack icon;

    public IntFeature(ItemStack icon, FeatureGroup parent, String name, Integer value) {
        super(parent, name, IntegerArgumentType.integer(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public boolean scroll(ServerConfigScreen.Node node, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        setUpdateSendClient(get() + (int) verticalAmount);
        return true;
    }

    @Override
    public void render(ServerConfigScreen.Node node, DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = Text.literal(get().toString());
        context.drawTextWithShadow(node.textRenderer(), text, node.getX() + node.getWidth() - node.textRenderer().getWidth(text) - 8, node.getY() + (node.getHeight() - node.textRenderer().fontHeight) / 2, 0xFFFFFFFF);
    }

    @Override
    public Codec<Integer> codec() {
        return Codec.INT;
    }

    @Override
    public PacketCodec<ByteBuf, Integer> packetCodec() {
        return PacketCodecs.INTEGER;
    }
}
