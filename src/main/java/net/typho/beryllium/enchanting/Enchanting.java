package net.typho.beryllium.enchanting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.armor.Armor;
import net.typho.beryllium.armor.ArmorTrimMaterialEffect;
import net.typho.beryllium.armor.ArmorTrimPatternEffect;
import net.typho.beryllium.armor.CustomTrimEffect;
import net.typho.beryllium.config.Config;
import net.typho.beryllium.util.Constructor;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Enchanting implements ModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("enchanting");

    public static final RegistryKey<Registry<EnchantmentInfo>> ENCHANTMENT_INFO_KEY = RegistryKey.ofRegistry(Beryllium.CONSTRUCTOR.id("enchantment_info"));
    public static final Registry<EnchantmentInfo> ENCHANTMENT_INFO = FabricRegistryBuilder.createSimple(ENCHANTMENT_INFO_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

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
        float f = stack.getItem().getEnchantability();

        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim != null) {
            ArmorTrimMaterialEffect materialEffect = Armor.ARMOR_TRIM_MATERIAL_EFFECTS.get(trim.getMaterial().getKey().orElseThrow().getValue());
            ArmorTrimPatternEffect patternEffect = Armor.ARMOR_TRIM_PATTERN_EFFECTS.get(trim.getPattern().getKey().orElseThrow().getValue());

            if (materialEffect != null) {
                for (CustomTrimEffect custom : materialEffect.custom()) {
                    f += custom.bonusEnchantmentCapacity(stack, trim);
                }
            }

            if (patternEffect != null) {
                for (CustomTrimEffect custom : patternEffect.custom()) {
                    f += custom.bonusEnchantmentCapacity(stack, trim);
                }
            }

            if (materialEffect != null) {
                if (stack.getItem() instanceof ArmorItem armor && materialEffect.debuffed().contains(armor.getMaterial().getKey().orElseThrow())) {
                    f *= 0.5f;
                }

                for (CustomTrimEffect custom : materialEffect.custom()) {
                    f *= custom.trimMaterialScale(stack, trim);
                }
            }

            if (patternEffect != null) {
                for (CustomTrimEffect custom : patternEffect.custom()) {
                    f *= custom.trimMaterialScale(stack, trim);
                }
            }
        }

        //ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        //if (trim != null && trim.getMaterial().isIn(Armor.ENCHANTMENT_MATERIALS)) {
        //    i += (int) (4 * Armor.trimMaterialScale(stack, null));
        //}

        return (int) f;
    }

    public static int getUsedEnchCapacity(ItemStack stack) {
        return getUsedEnchCapacity(EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries().stream().map(entry -> new EnchantmentLevelEntry(entry.getKey(), entry.getIntValue())));
    }

    public static int getUsedEnchCapacity(Stream<EnchantmentLevelEntry> stream) {
        return stream.mapToInt(entry -> getEnchantmentSize(entry.enchantment.value())).sum();
    }

    public static int getEnchantmentSize(Enchantment enchant) {
        return ((HasEnchantmentInfo) (Object) enchant).getInfo().size();
    }

    public static boolean canFitEnchantment(ItemStack stack, Enchantment enchant) {
        return canFitEnchantment(stack, enchant, () -> EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries().stream().map(entry -> new EnchantmentLevelEntry(entry.getKey(), entry.getIntValue())));
    }

    public static boolean canFitEnchantment(ItemStack stack, Enchantment enchant, Supplier<Stream<EnchantmentLevelEntry>> enchantments) {
        if (!Config.enchantmentCapacity.get()) {
            return true;
        }

        if (stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK)) {
            return enchantments.get().findAny().isEmpty();
        }

        if (enchantments.get().anyMatch(entry -> entry.enchantment.value() == enchant)) {
            return true;
        }

        return getUsedEnchCapacity(enchantments.get()) + getEnchantmentSize(enchant) <= getMaxEnchCapacity(stack);
    }

    public static ItemStack getCatalyst(RegistryEntry<Enchantment> enchant, int level) {
        if (!Config.enchantmentCatalysts.get()) {
            return ItemStack.EMPTY;
        }

        return Objects.requireNonNull(ENCHANTMENT_INFO.get(enchant.getKey().orElseThrow().getValue())).getCatalyst(level);
    }

    public static boolean hasEnoughCatalysts(ItemStack source, RegistryEntry<Enchantment> enchant, int level, PlayerEntity player) {
        if (player.getAbilities().creativeMode || !Config.enchantmentCatalysts.get()) {
            return true;
        }

        ItemStack req = getCatalyst(enchant, level);

        if (req.isEmpty()) {
            return true;
        }

        return source.getItem() == req.getItem() && source.getCount() >= req.getCount();
    }

    @Override
    public void onInitialize() {
        EnchantmentEvents.MODIFY.register((key, builder, source) -> ((HasEnchantmentInfo) builder).setInfo(ENCHANTMENT_INFO.get(key.getValue())));
    }
}
