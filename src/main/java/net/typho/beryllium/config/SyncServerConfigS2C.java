package net.typho.beryllium.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;

public record SyncServerConfigS2C(Identifier id, String value) implements CustomPayload {
    public SyncServerConfigS2C(Feature<?> feature) {
        this(feature.id, feature.value.toString());
    }

    public static final Id<SyncServerConfigS2C> ID = new Id<>(Beryllium.SYNC_SERVER_CONFIG_ID);
    public static final MapCodec<SyncServerConfigS2C> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(SyncServerConfigS2C::id),
            Codec.STRING.fieldOf("value").forGetter(SyncServerConfigS2C::value)
    ).apply(instance, SyncServerConfigS2C::new));
    public static final PacketCodec<PacketByteBuf, SyncServerConfigS2C> PACKET_CODEC = PacketCodecs.codec(CODEC.codec()).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
