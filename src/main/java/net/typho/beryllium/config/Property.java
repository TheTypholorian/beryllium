package net.typho.beryllium.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;

public abstract class Property<O> {
    public final Identifier id;
    public final ArgumentType<O> argumentType;
    public final O defValue;
    protected O value;

    public Property(Identifier id, ArgumentType<O> argumentType, O value) {
        this.id = id;
        this.argumentType = argumentType;
        this.value = defValue = value;
        Config.properties.put(id, this);
    }

    public O get() {
        return value;
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

    public abstract void read(OptionalDynamic<?> dynamic);

    public abstract void decode(ByteBuf buf);

    public abstract NbtElement write(DynamicRegistryManager registries);

    public abstract void encode(ByteBuf buf);
}
