package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;

public class BooleanFeature extends Feature<Boolean> {
    public final ItemStack icon;

    public BooleanFeature(ItemStack icon, FeatureGroup parent, String name, Boolean value) {
        super(parent, name, BoolArgumentType.bool(), value);
        this.icon = icon;
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public void click(ServerConfigScreen screen) {
        set(!get());
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
