package net.typho.beryllium.config;

import com.google.gson.JsonObject;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

public class KeyArgumentSerializer implements ArgumentSerializer<KeyArgumentType, KeyArgumentSerializer.Properties> {
    private final ServerConfig config;

    public KeyArgumentSerializer(ServerConfig config) {
        this.config = config;
    }

    @Override
    public void writePacket(Properties properties, PacketByteBuf buf) {
    }

    @Override
    public Properties fromPacket(PacketByteBuf buf) {
        return new Properties();
    }

    @Override
    public void writeJson(Properties properties, JsonObject json) {
    }

    @Override
    public Properties getArgumentTypeProperties(KeyArgumentType argumentType) {
        return new Properties();
    }

    public final class Properties implements ArgumentTypeProperties<KeyArgumentType> {
        public Properties() {
        }

        @Override
        public KeyArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return new KeyArgumentType(config);
        }

        @Override
        public ArgumentSerializer<KeyArgumentType, ?> getSerializer() {
            return KeyArgumentSerializer.this;
        }
    }
}
