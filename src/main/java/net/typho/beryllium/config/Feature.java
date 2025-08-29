package net.typho.beryllium.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;

public abstract class Feature<O> implements FeatureGroupChild {
    public final Identifier id;
    public final String name;
    public final FeatureGroup parent;
    public final ArgumentType<O> argumentType;
    public final O defValue;
    protected O value;

    public Feature(FeatureGroup parent, String name, ArgumentType<O> argumentType, O value) {
        this.id = parent.id.withSuffixedPath("/" + name);
        this.name = name;
        this.parent = parent;
        this.argumentType = argumentType;
        this.value = defValue = value;
        parent.features.add(this);
        ServerConfig.ALL_FEATURES.put(id, this);
    }

    public O get() {
        return value;
    }

    public void updatedClient(MinecraftClient client) {
    }

    public void updatedServer(MinecraftServer server) {
    }

    @SuppressWarnings("unchecked")
    public void set(Object value) {
        this.value = (O) value;
    }

    public void set(String s) {
        try {
            set(argumentType.parse(new StringReader(s)));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String commandPath() {
        if (id.getNamespace().equals(Beryllium.MOD_ID)) {
            return id.getPath();
        }

        return id.toString();
    }

    public String translationKey() {
        return id.toTranslationKey("config").replace('/', '.');
    }

    public Text display() {
        return Text.translatable(translationKey());
    }

    public abstract Codec<O> codec();

    public abstract PacketCodec<ByteBuf, O> packetCodec();

    @Override
    public void add(FeatureGroup group) {
        group.features.add(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + id.toString() + " = " + value.toString();
    }
}
