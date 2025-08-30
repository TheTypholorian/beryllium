package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.DynamicRegistryManager;

public class IntFeature extends Feature<Integer> {
    public final ItemStack icon;

    public IntFeature(ItemStack icon, FeatureGroup parent, String name, Integer value) {
        super(parent, name, IntegerArgumentType.integer(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen screen) {
        System.out.println("implement int features dumbass");
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
