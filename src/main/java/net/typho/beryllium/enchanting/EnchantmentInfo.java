package net.typho.beryllium.enchanting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public record EnchantmentInfo(ItemStack catalyst, int capacity) {
    public static final EnchantmentInfo DEFAULT = new EnchantmentInfo(ItemStack.EMPTY, 2);
    public static final Map<Identifier, EnchantmentInfo> MAP = new LinkedHashMap<>();

    public static EnchantmentInfo get(Identifier enchantment) {
        return MAP.getOrDefault(enchantment, DEFAULT);
    }

    public EnchantmentInfo(Item catalyst, int count, int capacity) {
        this(new ItemStack(catalyst, count), capacity);
    }

    public ItemStack catalyst(int level) {
        return catalyst.copyWithCount(catalyst.getCount() * level);
    }
}
