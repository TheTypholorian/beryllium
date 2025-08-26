package net.typho.beryllium.armor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPatterns;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.util.FreeEntityAttribute;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Armor implements ModInitializer {
    public static final RegistryKey<Registry<ArmorTrimPatternEffect>> ARMOR_TRIM_PATTERN_EFFECTS_KEY = RegistryKey.ofRegistry(Beryllium.ARMOR_CONSTRUCTOR.id("armor_trim_pattern_effects"));
    public static final Registry<ArmorTrimPatternEffect> ARMOR_TRIM_PATTERN_EFFECTS = FabricRegistryBuilder.createSimple(ARMOR_TRIM_PATTERN_EFFECTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    public static final RegistryKey<Registry<ArmorTrimMaterialEffect>> ARMOR_TRIM_MATERIAL_EFFECTS_KEY = RegistryKey.ofRegistry(Beryllium.ARMOR_CONSTRUCTOR.id("armor_trim_material_effects"));
    public static final Registry<ArmorTrimMaterialEffect> ARMOR_TRIM_MATERIAL_EFFECTS = FabricRegistryBuilder.createSimple(ARMOR_TRIM_MATERIAL_EFFECTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    public static final Map<SmithingTemplateItem, Identifier> SMITHING_TEMPLATE_PATTERNS = new LinkedHashMap<>();

    public static final RegistryEntry<EntityAttribute> GENERIC_RANGED_DAMAGE = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.ranged_damage", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.ranged_damage", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_SWIMMING_SPEED = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.swimming_speed", new FreeEntityAttribute("attribute.beryllium.armor.name.player.swimming_speed", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_REGENERATION = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.regeneration", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.regeneration", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_CLEANSING = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.cleansing", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.cleansing", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_GAMMA = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.gamma", new FreeEntityAttribute("attribute.beryllium.armor.name.player.gamma", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_MOUNT_REGENERATION = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.mount_regeneration", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.mount_regeneration", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_XP_GAIN = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.xp_gain", new FreeEntityAttribute("attribute.beryllium.armor.name.player.xp_gain", 1).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_XP_COST = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.xp_cost", new FreeEntityAttribute("attribute.beryllium.armor.name.player.xp_cost", 1).setCategory(EntityAttribute.Category.NEGATIVE).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_MOUNT_SPEED = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.mount_speed", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.mount_speed", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_SLIDING = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.sliding", new FreeEntityAttribute("attribute.beryllium.armor.name.player.sliding", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_DISCOUNT = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.discount", new FreeEntityAttribute("attribute.beryllium.armor.name.player.discount", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_SATURATION = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.saturation", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.saturation", 0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_BLOCK_SLIPPERINESS = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.block_slipperiness", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.block_slipperiness", 1).setCategory(EntityAttribute.Category.NEUTRAL).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_RANGED_SPEED = Beryllium.ARMOR_CONSTRUCTOR.attribute("generic.ranged_speed", new FreeEntityAttribute("attribute.beryllium.armor.name.generic.ranged_speed", 1).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_AIR_MINING_EFFICIENCY = Beryllium.ARMOR_CONSTRUCTOR.attribute("player.air_mining_efficiency", new ClampedEntityAttribute("attribute.beryllium.armor.name.player.air_mining_efficiency", 0.2, 0.00001, 1).setTracked(true));

    public static float trimMaterialScale(ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);
        float f = 1;

        if (trim != null) {
            ArmorTrimMaterialEffect materialEffect = ARMOR_TRIM_MATERIAL_EFFECTS.get(trim.getMaterial().getKey().orElseThrow().getValue());

            if (materialEffect != null) {
                if (stack.getItem() instanceof ArmorItem armor && materialEffect.debuffed().contains(armor.getMaterial().getKey().orElseThrow())) {
                    f *= 0.5f;
                }

                for (CustomTrimEffect custom : materialEffect.custom()) {
                    f *= custom.trimMaterialScale(stack, trim);
                }
            }

            ArmorTrimPatternEffect patternEffect = ARMOR_TRIM_PATTERN_EFFECTS.get(trim.getPattern().getKey().orElseThrow().getValue());

            if (patternEffect != null) {
                for (CustomTrimEffect custom : patternEffect.custom()) {
                    f *= custom.trimMaterialScale(stack, trim);
                }
            }
        }

        return f;
    }

    public static float trimPatternScale(ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);
        float f = 1;

        if (trim != null) {
            ArmorTrimMaterialEffect materialEffect = ARMOR_TRIM_MATERIAL_EFFECTS.get(trim.getMaterial().getKey().orElseThrow().getValue());

            if (materialEffect != null) {
                for (CustomTrimEffect custom : materialEffect.custom()) {
                    f *= custom.trimPatternScale(stack, trim);
                }
            }

            ArmorTrimPatternEffect patternEffect = ARMOR_TRIM_PATTERN_EFFECTS.get(trim.getPattern().getKey().orElseThrow().getValue());

            if (patternEffect != null) {
                for (CustomTrimEffect custom : patternEffect.custom()) {
                    f *= custom.trimPatternScale(stack, trim);
                }
            }
        }

        return f;
    }

    public static Optional<ArmorTrimPatternEffect> getTrimPatternEffect(ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim != null) {
            return ARMOR_TRIM_PATTERN_EFFECTS.getOrEmpty(trim.getPattern().getKey().orElseThrow().getValue());
        }

        return Optional.empty();
    }

    public static Optional<ArmorTrimMaterialEffect> getTrimMaterialEffect(ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim != null) {
            return ARMOR_TRIM_MATERIAL_EFFECTS.getOrEmpty(trim.getMaterial().getKey().orElseThrow().getValue());
        }

        return Optional.empty();
    }

    public static float bonusRangedDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(GENERIC_RANGED_DAMAGE);
    }

    public static float bonusSwimmingSpeed(LivingEntity entity) {
        return (float) entity.getAttributeValue(PLAYER_SWIMMING_SPEED);
    }

    public static float bonusHealing(LivingEntity entity) {
        float f = (float) entity.getAttributeValue(GENERIC_REGENERATION);
        LivingEntity rider = entity.getControllingPassenger();

        if (rider != null) {
            f += (float) rider.getAttributeValue(GENERIC_MOUNT_REGENERATION);
        }

        return f;
    }

    public static float bonusCleansing(LivingEntity entity) {
        return (float) entity.getAttributeValue(GENERIC_CLEANSING);
    }

    public static float bonusSight(LivingEntity entity) {
        return (float) entity.getAttributeValue(PLAYER_GAMMA);
    }

    public static int bonusReputation(LivingEntity entity) {
        return (int) entity.getAttributeValue(PLAYER_DISCOUNT);
    }

    public static int bonusSaturation(LivingEntity entity) {
        return (int) entity.getAttributeValue(GENERIC_SATURATION);
    }

    public static float bonusMountSpeed(Entity entity) {
        float f = 0;

        LivingEntity passenger = entity.getControllingPassenger();

        if (passenger != null) {
            f = (float) passenger.getAttributeValue(GENERIC_MOUNT_SPEED);
        }

        return f;
    }

    public static boolean shouldRenderArmor(boolean invisible, ItemStack stack, @Nullable ArmorTrim trim) {
        if (trim != null) {
            ArmorTrimMaterialEffect materialEffect = ARMOR_TRIM_MATERIAL_EFFECTS.get(trim.getMaterial().getKey().orElseThrow().getValue());

            if (materialEffect != null) {
                for (CustomTrimEffect custom : materialEffect.custom()) {
                    if (!custom.shouldRender(invisible, stack, trim)) {
                        return false;
                    }
                }
            }

            ArmorTrimPatternEffect patternEffect = ARMOR_TRIM_PATTERN_EFFECTS.get(trim.getPattern().getKey().orElseThrow().getValue());

            if (patternEffect != null) {
                for (CustomTrimEffect custom : patternEffect.custom()) {
                    if (!custom.shouldRender(invisible, stack, trim)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void onInitialize() {
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.SENTRY, GENERIC_RANGED_DAMAGE, 0.5, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.DUNE, EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.COAST, EntityAttributes.GENERIC_ATTACK_DAMAGE, 1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.TIDE, PLAYER_SWIMMING_SPEED, 0.02, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.WILD, GENERIC_REGENERATION, 0.02, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.WARD, EntityAttributes.GENERIC_ARMOR, 1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.VEX, EntityAttributes.GENERIC_ATTACK_SPEED, 0.05, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.RIB, EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.SNOUT, GENERIC_CLEANSING, 0.2, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.EYE, PLAYER_GAMMA, 1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.SPIRE, EntityAttributes.GENERIC_LUCK, 1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.WAYFINDER, GENERIC_MOUNT_REGENERATION, 0.03, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.RAISER, PLAYER_XP_GAIN, 0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.SHAPER, PLAYER_XP_COST, -0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.HOST, GENERIC_MOUNT_SPEED, 0.05, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.SILENCE, new CustomTrimEffect.Silence());
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.FLOW, GENERIC_BLOCK_SLIPPERINESS, 0.075, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimPatternEffect(ArmorTrimPatterns.BOLT, GENERIC_RANGED_SPEED, 0.25, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.AMETHYST, EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO, 0.1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.COPPER, EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE, 0.5, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.DIAMOND, new CustomTrimEffect.Diamond(), ArmorMaterials.DIAMOND.getKey().orElseThrow());
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.EMERALD, PLAYER_DISCOUNT, 5, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.GOLD, new CustomTrimEffect.Gold(), ArmorMaterials.GOLD.getKey().orElseThrow());
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.IRON, EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.LAPIS, EntityAttributes.GENERIC_JUMP_STRENGTH, 0.1, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.QUARTZ, EntityAttributes.PLAYER_MINING_EFFICIENCY, 8, EntityAttributeModifier.Operation.ADD_VALUE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.NETHERITE, EntityAttributes.GENERIC_BURNING_TIME, -0.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        Beryllium.ARMOR_CONSTRUCTOR.trimMaterialEffect(ArmorTrimMaterials.REDSTONE, GENERIC_SATURATION, 0.2, EntityAttributeModifier.Operation.ADD_VALUE);
    }
}
