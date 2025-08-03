package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.registry.DynamicRegistryManager;
import net.typho.beryllium.util.Constructor;

public class FloatProperty extends TextFieldProperty<Float> {
    public FloatProperty(ServerConfig config, String category, String name, Float value) {
        super(config, category, name, FloatArgumentType.floatArg(), value);
    }

    public FloatProperty(ServerConfig config, Constructor cons, String name, Float value) {
        super(config, cons, name, FloatArgumentType.floatArg(), value);
    }

    public FloatProperty(ServerConfig config, String name, Float value) {
        super(config, name, FloatArgumentType.floatArg(), value);
    }

    @Override
    public void read(OptionalDynamic<?> dynamic) {
        value = dynamic.asFloat(value);
    }

    @Override
    public void decode(ByteBuf buf) {
        value = buf.readFloat();
    }

    @Override
    public NbtElement write(DynamicRegistryManager registries) {
        return NbtFloat.of(value);
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeFloat(value);
    }
}
