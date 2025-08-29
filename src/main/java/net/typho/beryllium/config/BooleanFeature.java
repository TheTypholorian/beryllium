package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class BooleanFeature extends Feature<Boolean> {
    public final Codec<Boolean> codec;

    public BooleanFeature(FeatureGroup parent, String name, Boolean value) {
        super(parent, name, BoolArgumentType.bool(), value);
        codec = Codec.BOOL.fieldOf(id.toString()).codec();
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
