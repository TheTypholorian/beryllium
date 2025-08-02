package net.typho.beryllium.enchanting;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.util.Constructor;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class Enchanting implements ModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("enchanting");

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

    /**
     * This is an old system meant to buff gold gear, but I removed it cus it was unbalanced (tho you can still mixin and use it)
     */
    public static int getExtraLevels(ItemStack stack) {
        /*
        if (stack.getItem() instanceof ToolItem tool) {
            if (tool.getMaterial() == ToolMaterials.GOLD) {
                return 1;
            }
        } else if (stack.getItem() instanceof ArmorItem armor) {
            if (armor.getMaterial() == ArmorMaterials.GOLD) {
                return 1;
            }
        }
         */

        return 0;
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
        // TODO
        return 2;//BalancedEnchantment.cast(enchant.definition()).getCapacity();
    }

    public static boolean canFitEnchantment(ItemStack stack, Enchantment enchant) {
        return canFitEnchantment(stack, enchant, () -> EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries().stream().map(entry -> new EnchantmentLevelEntry(entry.getKey(), entry.getIntValue())));
    }

    public static boolean canFitEnchantment(ItemStack stack, Enchantment enchant, Supplier<Stream<EnchantmentLevelEntry>> enchantments) {
        if (!Beryllium.CONFIG.enchanting.capacity) {
            return true;
        }

        if (stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK)) {
            return enchantments.get().findAny().isEmpty();
        }

        if (enchantments.get().anyMatch(entry -> entry.enchantment.value() == enchant)) {
            return true;
        }

        return getUsedEnchCapacity(enchantments.get()) + getEnchantmentCapacity(enchant) <= getMaxEnchCapacity(stack);
    }

    public static ItemStack getCatalyst(RegistryEntry<Enchantment> enchant, int level) {
        if (!Beryllium.CONFIG.enchanting.catalysts) {
            return ItemStack.EMPTY;
        }

        //BalancedEnchantment balanced = BalancedEnchantment.cast(enchant.value().definition());
        //return new ItemStack(balanced.getCatalyst(), balanced.getCatalystCount() * level);
        return EnchantmentInfo.get(enchant.getKey().orElseThrow().getValue()).catalyst(level);
    }

    public static boolean hasEnoughCatalysts(ItemStack source, RegistryEntry<Enchantment> enchant, int level, PlayerEntity player) {
        if (player.getAbilities().creativeMode || !Beryllium.CONFIG.enchanting.catalysts) {
            return true;
        }

        ItemStack req = getCatalyst(enchant, level);

        return source.getItem() == req.getItem() && source.getCount() >= req.getCount();
    }

    @Override
    public void onInitialize() {
    }

    public static class Config extends ConfigSection {
        public boolean catalysts = true;
        public boolean capacity = true;
    }
}
