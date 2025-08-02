package net.typho.beryllium.config;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServerConfig {
    public static final PacketCodec<ByteBuf, ServerConfig> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public ServerConfig decode(ByteBuf buf) {
            return new ServerConfig(buf);
        }

        @Override
        public void encode(ByteBuf buf, ServerConfig config) {
            config.encode(buf);
        }
    };
    public final Map<String, Property<?>> properties = new LinkedHashMap<>();
    public BooleanProperty durabilityRemoval = new BooleanProperty("durabilityRemoval", true);

    public ServerConfig() {
    }

    public ServerConfig(ByteBuf buf) {
        for (Property<?> property : properties.values()) {
            property.decode(buf);
        }
    }

    public void read(Dynamic<?> dynamic) {
        for (Property<?> property : properties.values()) {
            property.read(dynamic.get(property.name));
        }
    }

    public NbtElement write(DynamicRegistryManager registries) {
        NbtCompound nbt = new NbtCompound();

        for (Property<?> property : properties.values()) {
            nbt.put(property.name, property.write(registries));
        }

        return nbt;
    }

    public void encode(ByteBuf buf) {
        for (Property<?> property : properties.values()) {
            property.encode(buf);
        }
    }

    public Text print() {
        MutableText text = Text.translatable("config.beryllium.title");

        for (Property<?> property : properties.values()) {
            text.append(Text.literal("\n").append(property.display()).append(" = ").append(property.value.toString()));
        }

        return text;
    }

    public void save(MinecraftServer server) {
        server.session.backupLevelDataFile(server.getRegistryManager(), server.getSaveProperties(), server.getPlayerManager().getUserData());

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new SyncServerConfigS2C(this));
        }
    }

    public ArgumentType<String> keyArgumentType() {
        return new KeyArgumentType();
    }

    public LiteralArgumentBuilder<ServerCommandSource> setCommandBuilder() {
        LiteralArgumentBuilder<ServerCommandSource> builder = LiteralArgumentBuilder.literal("set");

        for (Property<?> property : properties.values()) {
            builder.then(
                    LiteralArgumentBuilder.<ServerCommandSource>literal(property.name)
                            .then(
                                    CommandManager.argument("value", property.argumentType)
                                            .executes(context -> {
                                                Object value = context.getArgument("value", Object.class);
                                                property.set(value);
                                                save(context.getSource().getServer());
                                                context.getSource().sendFeedback(() -> property.display().copy().append(" = ").append(property.get().toString()), false);
                                                return 1;
                                            })
                            )
            );
        }

        return builder;
    }

    public class KeyArgumentType implements ArgumentType<String> {
        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            return reader.readString();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(properties.keySet(), builder);
        }
    }

    public class KeyArgumentSerializer implements ArgumentSerializer<KeyArgumentType, KeyArgumentSerializer.Properties> {
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

        public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<KeyArgumentType> {
            public Properties() {
            }

            @Override
            public KeyArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
                return new KeyArgumentType();
            }

            @Override
            public ArgumentSerializer<KeyArgumentType, ?> getSerializer() {
                return KeyArgumentSerializer.this;
            }
        }
    }

    public abstract class Property<O> {
        public final String name;
        public final ArgumentType<?> argumentType;
        protected O value;

        public Property(String name, ArgumentType<?> argumentType) {
            this.name = name;
            this.argumentType = argumentType;
            properties.put(name, this);
        }

        public Property(String name, ArgumentType<?> argumentType, O value) {
            this(name, argumentType);
            this.value = value;
        }

        public O get() {
            return value;
        }

        @SuppressWarnings("unchecked")
        public void set(Object value) {
            this.value = (O) value;
        }

        public String translationKey() {
            return "config.beryllium." + name;
        }

        public Text display() {
            return Text.translatable(translationKey());
        }

        public abstract void read(OptionalDynamic<?> dynamic);

        public abstract void decode(ByteBuf buf);

        public abstract NbtElement write(DynamicRegistryManager registries);

        public abstract void encode(ByteBuf buf);
    }

    public class BooleanProperty extends Property<Boolean> {
        public BooleanProperty(String name) {
            super(name, BoolArgumentType.bool());
        }

        public BooleanProperty(String name, Boolean value) {
            super(name, BoolArgumentType.bool(), value);
        }

        @Override
        public void read(OptionalDynamic<?> dynamic) {
            value = dynamic.asBoolean(value);
        }

        @Override
        public void decode(ByteBuf buf) {
            value = buf.readBoolean();
        }

        @Override
        public NbtElement write(DynamicRegistryManager registries) {
            return NbtByte.of(value);
        }

        @Override
        public void encode(ByteBuf buf) {
            buf.writeBoolean(value);
        }
    }
}
