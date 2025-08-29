package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class FloatFeature extends Feature<Float> {
    public final Codec<Float> codec;

    public FloatFeature(FeatureGroup parent, String name, Float value) {
        super(parent, name, FloatArgumentType.floatArg(), value);
        codec = Codec.FLOAT.fieldOf(id.toString()).codec();
    }

    @Override
    public Codec<Float> codec() {
        return codec;
    }

    @Override
    public PacketCodec<ByteBuf, Float> packetCodec() {
        return PacketCodecs.FLOAT;
    }
}
