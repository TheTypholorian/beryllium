package net.typho.beryllium.config;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;

public record SyncServerConfigS2C<T>(Feature<T> feature, T value) implements CustomPayload {
    public SyncServerConfigS2C(Feature<T> feature) {
        this(feature, feature.value);
    }

    @SuppressWarnings("unchecked")
    public static <T> SyncServerConfigS2C<T> cast(Feature<?> feature, Object value) {
        return new SyncServerConfigS2C<>((Feature<T>) feature, (T) value);
    }

    public void encode(PacketByteBuf buf) {
        Identifier.PACKET_CODEC.encode(buf, feature.id);
        feature.packetCodec().encode(buf, value);
    }

    public static final Id<SyncServerConfigS2C<?>> ID = new Id<>(Beryllium.SYNC_SERVER_CONFIG_ID);
    public static final PacketCodec<PacketByteBuf, SyncServerConfigS2C<?>> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public SyncServerConfigS2C<?> decode(PacketByteBuf buf) {
            Identifier id = Identifier.PACKET_CODEC.decode(buf);
            Feature<?> feature = ServerConfig.ALL_FEATURES.get(id);

            if (feature == null) {
                throw new NullPointerException("No feature with id " + id);
            }

            return SyncServerConfigS2C.cast(feature, feature.packetCodec().decode(buf));
        }

        @Override
        public void encode(PacketByteBuf buf, SyncServerConfigS2C<?> packet) {
            packet.encode(buf);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
