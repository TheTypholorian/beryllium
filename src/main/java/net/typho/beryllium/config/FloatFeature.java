package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.registry.DynamicRegistryManager;

public class FloatFeature extends Feature<Float> {
    public final ItemStack icon;

    public FloatFeature(ItemStack icon, FeatureGroup parent, String name, Float value) {
        super(parent, name, FloatArgumentType.floatArg(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen screen) {
        System.out.println("implement float features dumbass");
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
