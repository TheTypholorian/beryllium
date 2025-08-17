package net.typho.beryllium.enchanting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public record EnchantmentInfo(ItemStack catalyst, int size) {
    public static final EnchantmentInfo DEFAULT = new EnchantmentInfo(ItemStack.EMPTY, 3);
    public static final MapCodec<EnchantmentInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("catalyst").forGetter(EnchantmentInfo::catalyst),
            Codec.INT.fieldOf("size").forGetter(EnchantmentInfo::size)
    ).apply(instance, EnchantmentInfo::new));

    public EnchantmentInfo(Item catalyst, int count, int size) {
        this(new ItemStack(catalyst, count), size);
    }

    public EnchantmentInfo(Item catalyst, int size) {
        this(catalyst, 4, size);
    }

    public ItemStack getCatalyst(int level) {
        return catalyst.copyWithCount(catalyst.getCount() * level);
    }
}
