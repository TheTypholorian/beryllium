package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

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
    public Codec<Integer> codec() {
        return Codec.INT;
    }

    @Override
    public PacketCodec<ByteBuf, Integer> packetCodec() {
        return PacketCodecs.INTEGER;
    }
}
