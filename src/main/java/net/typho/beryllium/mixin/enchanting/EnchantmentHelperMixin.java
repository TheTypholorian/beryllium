package net.typho.beryllium.mixin.enchanting;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.stream.Stream;

import static net.minecraft.enchantment.EnchantmentHelper.removeConflicts;

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

            for (int l = enchantment.getMaxLevel() + Beryllium.ENCHANTING.getExtraLevels(stack); l >= enchantment.getMinLevel(); l--) {
                if (level >= enchantment.getMinPower(l) && (level >= 25 || level <= enchantment.getMaxPower(l))) {
                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries()) {
                        if (entry.getKey().value() == enchantment) {
                            if (l < entry.getIntValue()) {
                                return;
                            } else if (l == entry.getIntValue() && enchantment.getMaxLevel() + Beryllium.ENCHANTING.getExtraLevels(stack) == l) {
                                return;
                            } else {
                                break;
                            }
                        }
                    }

                    list.add(new EnchantmentLevelEntry(enchantmentx, l));
                }
            }
        });

        return list;
    }

    /**
     * @author The Typhothanian
     * @reason Enchantment capacity
     */
    @Overwrite
    public static List<EnchantmentLevelEntry> generateEnchantments(
            Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments
    ) {
        List<EnchantmentLevelEntry> list = Lists.newArrayList();
        Item item = stack.getItem();
        int i = item.getEnchantability();

        if (i > 0) {
            level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
            level = MathHelper.clamp(Math.round(level + level * f), 1, Integer.MAX_VALUE);
            List<EnchantmentLevelEntry> list2 = getPossibleEntries(level, stack, possibleEnchantments);

            if (!list2.isEmpty()) {
                Weighting.getRandom(random, list2).ifPresent(add -> {
                    if (Beryllium.ENCHANTING.canFitEnchantment(stack, add.enchantment.value(), list::stream)) {
                        list.add(add);
                    }
                });

                while (random.nextInt(50) <= level) {
                    if (!list.isEmpty()) {
                        removeConflicts(list2, Util.getLast(list));
                    }

                    if (list2.isEmpty()) {
                        break;
                    }

                    Weighting.getRandom(random, list2).ifPresent(add -> {
                        if (Beryllium.ENCHANTING.canFitEnchantment(stack, add.enchantment.value(), list::stream)) {
                            list.add(add);
                        }
                    });
                    level /= 2;
                }
            }
        }

        return list;
    }
}
