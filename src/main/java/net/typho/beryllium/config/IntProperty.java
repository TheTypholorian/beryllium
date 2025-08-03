package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.DynamicRegistryManager;
import net.typho.beryllium.util.Constructor;

public class IntProperty extends TextFieldProperty<Integer> {
    public IntProperty(ServerConfig config, String category, String name, Integer value) {
        super(config, category, name, IntegerArgumentType.integer(), value);
    }

    public IntProperty(ServerConfig config, Constructor cons, String name, Integer value) {
        super(config, cons, name, IntegerArgumentType.integer(), value);
    }

    public IntProperty(ServerConfig config, String name, Integer value) {
        super(config, name, IntegerArgumentType.integer(), value);
    }

    @Override
    public void read(OptionalDynamic<?> dynamic) {
        value = dynamic.asInt(value);
    }

    @Override
    public void decode(ByteBuf buf) {
        value = buf.readInt();
    }

    @Override
    public NbtElement write(DynamicRegistryManager registries) {
        return NbtInt.of(value);
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(value);
    }
}
