package net.typho.beryllium.config;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.typho.beryllium.Beryllium;

public record SyncServerConfigS2C(ServerConfig config) implements CustomPayload {
    public SyncServerConfigS2C() {
        this(Beryllium.SERVER_CONFIG);
    }

    public static final Id<SyncServerConfigS2C> ID = new Id<>(Beryllium.SYNC_SERVER_CONFIG_ID);
    public static final PacketCodec<RegistryByteBuf, SyncServerConfigS2C> CODEC = PacketCodec.tuple(ServerConfig.PACKET_CODEC, SyncServerConfigS2C::config, SyncServerConfigS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
