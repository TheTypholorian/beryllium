package net.typho.beryllium.config;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;

public record SetConfigValuePacket<T>(ConfigOption<T> option, T value) implements CustomPayload {
    public SetConfigValuePacket(ConfigOption<T> option) {
        this(option, option.value);
    }

    @SuppressWarnings("unchecked")
    public static <T> SetConfigValuePacket<T> cast(ConfigOption<?> option, Object value) {
        return new SetConfigValuePacket<>((ConfigOption<T>) option, (T) value);
    }

    public void encode(PacketByteBuf buf) {
        Identifier.PACKET_CODEC.encode(buf, option.id);
        option.packetCodec().encode(buf, value);
    }

    public static final Id<SetConfigValuePacket<?>> ID = new Id<>(Beryllium.SYNC_SERVER_CONFIG_ID);
    public static final PacketCodec<PacketByteBuf, SetConfigValuePacket<?>> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public SetConfigValuePacket<?> decode(PacketByteBuf buf) {
            Identifier id = Identifier.PACKET_CODEC.decode(buf);
            ConfigOption<?> option = ServerConfig.ALL_OPTIONS.get(id);

            if (option == null) {
                System.err.println("No feature with id " + id);
                return null;
            }

            return SetConfigValuePacket.cast(option, option.packetCodec().decode(buf));
        }

        @Override
        public void encode(PacketByteBuf buf, SetConfigValuePacket<?> packet) {
            packet.encode(buf);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
