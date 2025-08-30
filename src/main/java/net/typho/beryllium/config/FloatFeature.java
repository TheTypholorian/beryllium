package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class FloatFeature extends Feature<Float> {
    public final Codec<Float> codec;
    public final ItemStack icon;

    public FloatFeature(ItemStack icon, FeatureGroup parent, String name, Float value) {
        super(parent, name, FloatArgumentType.floatArg(), value);
        codec = Codec.FLOAT.fieldOf(id.toString()).codec();
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen screen) {
        System.out.println("implement float features dumbass");
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
