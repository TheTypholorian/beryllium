package net.typho.beryllium.enchanting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.typho.beryllium.Module;

import java.util.stream.Stream;

public class Enchanting implements Module {
    @Override
    public void onInitialize() {
    }

    public static String toRomanNumeral(int n) {
        enum Letter {
            M(1000), D(500), C(100), L(50), X(10), V(5), I(1);

            public final int value;

            Letter(int value) {
                this.value = value;
            }
        }

        StringBuilder builder = new StringBuilder();

        for (Letter letter : Letter.values()) {
            int num = Math.floorDiv(n, letter.value);

            switch (num) {
                case 1, 2, 3: {
                    builder.append(letter.name().repeat(num));
                    break;
                }
                case 4: {
                    if (letter == Letter.M) {
                        builder.append("MMMM");
                    } else {
                        builder.append(letter.name()).append(Letter.values()[letter.ordinal() - 1]);
                    }
                    break;
                }
            }

            n -= letter.value * num;
        }

        return builder.toString();
    }

    public static int getMaxEnchCapacity(ItemStack stack) {
        return stack.getItem().getEnchantability();
    }

    public static int getUsedEnchCapacity(ItemStack stack) {
        return getUsedEnchCapacity(EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries().stream().map(entry -> new EnchantmentLevelEntry(entry.getKey(), entry.getIntValue())));
    }

    public static int getUsedEnchCapacity(Stream<EnchantmentLevelEntry> stream) {
        return stream.mapToInt(entry -> getEnchantmentCapacity(entry.enchantment.value())).sum();
    }

    public static int getEnchantmentCapacity(Enchantment enchant) {
        return BalancedEnchantment.cast(enchant.definition()).getCapacity();
    }

    public static boolean canFitEnchantment(ItemStack stack, Enchantment enchant) {
        return canFitEnchantment(stack, enchant, EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries().stream().map(entry -> new EnchantmentLevelEntry(entry.getKey(), entry.getIntValue())));
    }

    public static boolean canFitEnchantment(ItemStack stack, Enchantment enchant, Stream<EnchantmentLevelEntry> enchantments) {
        if (stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK)) {
            return enchantments.findAny().isEmpty();
        }

        if (enchantments.anyMatch(entry -> entry.enchantment.value() == enchant)) {
            return true;
        }

        return getUsedEnchCapacity(enchantments) + getEnchantmentCapacity(enchant) <= getMaxEnchCapacity(stack);
    }

    public static boolean hasEnoughCatalysts(ItemStack source, RegistryEntry<Enchantment> enchant, int level, PlayerEntity player) {
        if (player.getAbilities().creativeMode) {
            return true;
        }

        ItemStack req = getEnchantmentCatalyst(enchant, level);

        return source.getItem() == req.getItem() && source.getCount() >= req.getCount();
    }

    public static ItemStack getEnchantmentCatalyst(RegistryEntry<Enchantment> enchant, int level) {
        BalancedEnchantment balanced = BalancedEnchantment.cast(enchant.value().definition());
        return new ItemStack(balanced.getCatalyst(), balanced.getCatalystCount() * level);
    }
}
