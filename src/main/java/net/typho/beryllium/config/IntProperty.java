package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

public class IntProperty extends Property<Integer> {
    public IntProperty(Identifier id, Integer value) {
        super(id, IntegerArgumentType.integer(), value);
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
