package net.typho.beryllium.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.typho.beryllium.util.Constructor;

public abstract class Property<O> {
    private final ServerConfig config;
    public final String category;
    public final String name;
    public final ArgumentType<O> argumentType;
    public final O defValue;
    protected O value;

    public Property(ServerConfig config, String category, String name, ArgumentType<O> argumentType, O value) {
        this.config = config;
        this.category = category;
        this.name = name;
        this.argumentType = argumentType;
        config.properties.put(name, this);
        this.value = defValue = value;
    }

    public Property(ServerConfig config, Constructor cons, String name, ArgumentType<O> argumentType, O value) {
        this(config, cons.name, name, argumentType, value);
    }

    public Property(ServerConfig config, String name, ArgumentType<O> argumentType, O value) {
        this(config, (String) null, name, argumentType, value);
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

    @Environment(EnvType.CLIENT)
    public abstract void addWidget(GridWidget.Adder adder, int rows);
}
