package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class IntFeature extends Feature<Integer> {
    public final Codec<Integer> codec;
    public final ItemStack icon;

    public IntFeature(ItemStack icon, FeatureGroup parent, String name, Integer value) {
        super(parent, name, IntegerArgumentType.integer(), value);
        codec = Codec.INT.fieldOf(id.toString()).codec();
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen screen) {
        System.out.println("implement int features dumbass");
    }

    @Override
    public Codec<Integer> codec() {
        return codec;
    }

    @Override
    public PacketCodec<ByteBuf, Integer> packetCodec() {
        return PacketCodecs.INTEGER;
    }
}
