package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class BooleanFeature extends Feature<Boolean> {
    public final Codec<Boolean> codec;
    public final ItemStack icon;

    public BooleanFeature(ItemStack icon, FeatureGroup parent, String name, Boolean value) {
        super(parent, name, BoolArgumentType.bool(), value);
        codec = Codec.BOOL.fieldOf(id.toString()).codec();
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen screen) {
        set(!get());
    }

    @Override
    public Codec<Boolean> codec() {
        return codec;
    }

    @Override
    public PacketCodec<ByteBuf, Boolean> packetCodec() {
        return PacketCodecs.BOOL;
    }
}
