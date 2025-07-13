package net.typho.beryllium.mixin;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    /**
     * @author The Typhothanian
     * @reason Account for the stack already having enchantments
     */
    @Overwrite
    public static List<EnchantmentLevelEntry> getPossibleEntries(int level, ItemStack stack, Stream<RegistryEntry<Enchantment>> possibleEnchantments) {
        List<EnchantmentLevelEntry> list = Lists.newArrayList();
        boolean bl = stack.isOf(Items.BOOK);
        possibleEnchantments.filter(enchantment -> (enchantment.value().isPrimaryItem(stack) || bl) && enchantment.value().isAcceptableItem(stack)).forEach(enchantmentx -> {
            Enchantment enchantment = enchantmentx.value();

            for (int j = enchantment.getMaxLevel(); j >= enchantment.getMinLevel(); j--) {
                if (level >= enchantment.getMinPower(j) && level <= enchantment.getMaxPower(j)) {
                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries()) {
                        if (entry.getKey().value() == enchantment) {
                            if (j < entry.getIntValue()) {
                                return;
                            } else if (j == entry.getIntValue() && enchantment.getMaxLevel() == j) {
                                return;
                            } else {
                                break;
                            }
                        }
                    }

                    list.add(new EnchantmentLevelEntry(enchantmentx, j));
                    break;
                }
            }
        });
        return list;
    }
}
