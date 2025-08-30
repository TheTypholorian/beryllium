package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;

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
    public void init(ServerConfigScreen.Node node) {
        node.parent().addDrawableChild(
                ButtonWidget.builder(get() ? ScreenTexts.ON : ScreenTexts.OFF, button -> {
                            set(!get());
                            button.setMessage(get() ? ScreenTexts.ON : ScreenTexts.OFF);
                        })
                        .dimensions(node.getX() + node.getWidth() - 68, node.getY() + 4, 64, 16)
                        .build()
        );
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
