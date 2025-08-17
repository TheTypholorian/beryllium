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
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.armor.Armor;
import net.typho.beryllium.armor.ArmorTrimMaterialEffect;
import net.typho.beryllium.armor.ArmorTrimPatternEffect;
import net.typho.beryllium.armor.CustomTrimEffect;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.config.ServerConfig;
import net.typho.beryllium.exploring.Exploring;
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
    public static final EnchantmentInfo AQUA_AFFINITY = CONSTRUCTOR.enchantmentInfo(Identifier.of("aqua_affinity"), new EnchantmentInfo(Items.SOUL_SAND, 2));
    public static final EnchantmentInfo BANE_OF_ARTHROPODS = CONSTRUCTOR.enchantmentInfo(Identifier.of("bane_of_arthropods"), new EnchantmentInfo(Items.COBWEB, 3));
    public static final EnchantmentInfo CURSE_OF_BINDING = CONSTRUCTOR.enchantmentInfo(Identifier.of("curse_of_binding"), new EnchantmentInfo(Items.AIR, 3));
    public static final EnchantmentInfo BLAST_PROTECTION = CONSTRUCTOR.enchantmentInfo(Identifier.of("blast_protection"), new EnchantmentInfo(Items.OBSIDIAN, 4));
    public static final EnchantmentInfo BREACH = CONSTRUCTOR.enchantmentInfo(Identifier.of("breach"), new EnchantmentInfo(Items.GUNPOWDER, 4));
    public static final EnchantmentInfo CHANNELING = CONSTRUCTOR.enchantmentInfo(Identifier.of("channeling"), new EnchantmentInfo(Items.COPPER_INGOT, 4));
    public static final EnchantmentInfo DENSITY = CONSTRUCTOR.enchantmentInfo(Identifier.of("density"), new EnchantmentInfo(Items.GOLD_BLOCK, 4));
    public static final EnchantmentInfo DEPTH_STRIDER = CONSTRUCTOR.enchantmentInfo(Identifier.of("depth_strider"), new EnchantmentInfo(Items.PRISMARINE_SHARD, 2));
    public static final EnchantmentInfo EFFICIENCY = CONSTRUCTOR.enchantmentInfo(Identifier.of("efficiency"), new EnchantmentInfo(Items.COAL, 2));
    public static final EnchantmentInfo FEATHER_FALLING = CONSTRUCTOR.enchantmentInfo(Identifier.of("feather_falling"), new EnchantmentInfo(Items.FEATHER, 3));
    public static final EnchantmentInfo FIRE_ASPECT = CONSTRUCTOR.enchantmentInfo(Identifier.of("fire_aspect"), new EnchantmentInfo(Items.FIRE_CHARGE, 3));
    public static final EnchantmentInfo FIRE_PROTECTION = CONSTRUCTOR.enchantmentInfo(Identifier.of("fire_protection"), new EnchantmentInfo(Items.MAGMA_CREAM, 4));
    public static final EnchantmentInfo FLAME = CONSTRUCTOR.enchantmentInfo(Identifier.of("flame"), new EnchantmentInfo(Items.FIRE_CHARGE, 3));
    public static final EnchantmentInfo FORTUNE = CONSTRUCTOR.enchantmentInfo(Identifier.of("fortune"), new EnchantmentInfo(Items.EMERALD, 4));
    public static final EnchantmentInfo FROST_WALKER = CONSTRUCTOR.enchantmentInfo(Identifier.of("frost_walker"), new EnchantmentInfo(Items.ICE, 2));
    public static final EnchantmentInfo IMPALING = CONSTRUCTOR.enchantmentInfo(Identifier.of("impaling"), new EnchantmentInfo(Items.BONE, 3));
    public static final EnchantmentInfo INFINITY = CONSTRUCTOR.enchantmentInfo(Identifier.of("infinity"), new EnchantmentInfo(Items.SPECTRAL_ARROW, 4));
    public static final EnchantmentInfo KNOCKBACK = CONSTRUCTOR.enchantmentInfo(Identifier.of("knockback"), new EnchantmentInfo(Items.PISTON, 2));
    public static final EnchantmentInfo LOOTING = CONSTRUCTOR.enchantmentInfo(Identifier.of("looting"), new EnchantmentInfo(Items.EMERALD, 3));
    public static final EnchantmentInfo LOYALTY = CONSTRUCTOR.enchantmentInfo(Identifier.of("loyalty"), new EnchantmentInfo(Items.COMPASS, 4));
    public static final EnchantmentInfo LUCK_OF_THE_SEA = CONSTRUCTOR.enchantmentInfo(Identifier.of("luck_of_the_sea"), new EnchantmentInfo(Items.EMERALD, 3));
    public static final EnchantmentInfo LURE = CONSTRUCTOR.enchantmentInfo(Identifier.of("lure"), new EnchantmentInfo(Items.KELP, 2));
    public static final EnchantmentInfo MENDING = CONSTRUCTOR.enchantmentInfo(Identifier.of("mending"), new EnchantmentInfo(Items.AMETHYST_SHARD, 4));
    public static final EnchantmentInfo MULTISHOT = CONSTRUCTOR.enchantmentInfo(Identifier.of("multishot"), new EnchantmentInfo(Items.EMERALD, 4));
    public static final EnchantmentInfo PIERCING = CONSTRUCTOR.enchantmentInfo(Identifier.of("piercing"), new EnchantmentInfo(Items.BONE, 3));
    public static final EnchantmentInfo POWER = CONSTRUCTOR.enchantmentInfo(Identifier.of("power"), new EnchantmentInfo(Items.DIAMOND, 3));
    public static final EnchantmentInfo PROJECTILE_PROTECTION = CONSTRUCTOR.enchantmentInfo(Identifier.of("projectile_protection"), new EnchantmentInfo(Items.SNOWBALL, 4));
    public static final EnchantmentInfo PROTECTION = CONSTRUCTOR.enchantmentInfo(Identifier.of("protection"), new EnchantmentInfo(Items.IRON_INGOT, 4));
    public static final EnchantmentInfo PUNCH = CONSTRUCTOR.enchantmentInfo(Identifier.of("punch"), new EnchantmentInfo(Items.PISTON, 2));
    public static final EnchantmentInfo QUICK_CHARGE = CONSTRUCTOR.enchantmentInfo(Identifier.of("quick_charge"), new EnchantmentInfo(Items.REDSTONE, 3));
    public static final EnchantmentInfo RESPIRATION = CONSTRUCTOR.enchantmentInfo(Identifier.of("respiration"), new EnchantmentInfo(Items.SOUL_SAND, 2));
    public static final EnchantmentInfo RIPTIDE = CONSTRUCTOR.enchantmentInfo(Identifier.of("riptide"), new EnchantmentInfo(Items.PRISMARINE_SHARD, 4));
    public static final EnchantmentInfo SHARPNESS = CONSTRUCTOR.enchantmentInfo(Identifier.of("sharpness"), new EnchantmentInfo(Items.BLAZE_ROD, 3));
    public static final EnchantmentInfo SILK_TOUCH = CONSTRUCTOR.enchantmentInfo(Identifier.of("silk_touch"), new EnchantmentInfo(Items.STRING, 4));
    public static final EnchantmentInfo SMITE = CONSTRUCTOR.enchantmentInfo(Identifier.of("smite"), new EnchantmentInfo(Items.ROTTEN_FLESH, 3));
    public static final EnchantmentInfo SOUL_SPEED = CONSTRUCTOR.enchantmentInfo(Identifier.of("soul_speed"), new EnchantmentInfo(Items.SOUL_SOIL, 3));
    public static final EnchantmentInfo SWEEPING_EDGE = CONSTRUCTOR.enchantmentInfo(Identifier.of("sweeping_edge"), new EnchantmentInfo(Items.GUNPOWDER, 3));
    public static final EnchantmentInfo SWIFT_SNEAK = CONSTRUCTOR.enchantmentInfo(Identifier.of("swift_sneak"), new EnchantmentInfo(Items.SCULK, 3));
    public static final EnchantmentInfo THORNS = CONSTRUCTOR.enchantmentInfo(Identifier.of("thorns"), new EnchantmentInfo(Items.CACTUS, 4));
    public static final EnchantmentInfo UNBREAKING = CONSTRUCTOR.enchantmentInfo(Identifier.of("unbreaking"), new EnchantmentInfo(Items.OBSIDIAN, 2));
    public static final EnchantmentInfo CURSE_OF_VANISHING = CONSTRUCTOR.enchantmentInfo(Identifier.of("curse_of_vanishing"), new EnchantmentInfo(Items.AIR, 3));
    public static final EnchantmentInfo WIND_BURST = CONSTRUCTOR.enchantmentInfo(Identifier.of("wind_burst"), new EnchantmentInfo(Items.WIND_CHARGE, 4));

    public static final EnchantmentInfo ADRENALINE = CONSTRUCTOR.enchantmentInfo(Combat.CONSTRUCTOR.id("adrenaline"), new EnchantmentInfo(Items.SUGAR, 3));
    public static final EnchantmentInfo LIFESTEAL = CONSTRUCTOR.enchantmentInfo(Combat.CONSTRUCTOR.id("lifesteal"), new EnchantmentInfo(Items.GLISTERING_MELON_SLICE, 3));
    public static final EnchantmentInfo SHACKLING = CONSTRUCTOR.enchantmentInfo(Combat.CONSTRUCTOR.id("shackling"), new EnchantmentInfo(Items.CHAIN, 3));
    public static final EnchantmentInfo TARGET_LOCK = CONSTRUCTOR.enchantmentInfo(Combat.CONSTRUCTOR.id("target_lock"), new EnchantmentInfo(Items.GLOW_BERRIES, 4));
    public static final EnchantmentInfo VITALITY = CONSTRUCTOR.enchantmentInfo(Combat.CONSTRUCTOR.id("vitality"), new EnchantmentInfo(Items.GOLDEN_APPLE, 4));

    public static final EnchantmentInfo ENCUMBERED = CONSTRUCTOR.enchantmentInfo(Exploring.CONSTRUCTOR.id("encumbered"), new EnchantmentInfo(Items.IRON_BLOCK, 2, 2));
    public static final EnchantmentInfo REELING = CONSTRUCTOR.enchantmentInfo(Exploring.CONSTRUCTOR.id("reeling"), new EnchantmentInfo(Items.CHAIN, 4));
    public static final EnchantmentInfo REACH = CONSTRUCTOR.enchantmentInfo(Exploring.CONSTRUCTOR.id("reach"), new EnchantmentInfo(Items.STICK, 3));
    public static final EnchantmentInfo STABLE_FOOTING = CONSTRUCTOR.enchantmentInfo(Exploring.CONSTRUCTOR.id("stable_footing"), new EnchantmentInfo(Items.SLIME_BALL, 4));
    public static final EnchantmentInfo STEP = CONSTRUCTOR.enchantmentInfo(Exploring.CONSTRUCTOR.id("step"), new EnchantmentInfo(Items.SUGAR, 2));
    public static final EnchantmentInfo WEIGHTLESS = CONSTRUCTOR.enchantmentInfo(Exploring.CONSTRUCTOR.id("weightless"), new EnchantmentInfo(Items.FEATHER, 2));

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
        if (!ServerConfig.enchantmentCapacity.get()) {
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
        if (!ServerConfig.enchantmentCatalysts.get()) {
            return ItemStack.EMPTY;
        }

        return Objects.requireNonNull(ENCHANTMENT_INFO.get(enchant.getKey().orElseThrow().getValue())).getCatalyst(level);
    }

    public static boolean hasEnoughCatalysts(ItemStack source, RegistryEntry<Enchantment> enchant, int level, PlayerEntity player) {
        if (player.getAbilities().creativeMode || !ServerConfig.enchantmentCatalysts.get()) {
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
