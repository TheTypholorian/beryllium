package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

public class BooleanServerResourceProperty extends ServerResourceProperty<Boolean> {
    public BooleanServerResourceProperty(Identifier id, Boolean value) {
        super(id, BoolArgumentType.bool(), value);
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
