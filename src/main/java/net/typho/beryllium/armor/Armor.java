package net.typho.beryllium.armor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.typho.beryllium.util.Constructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Armor implements ModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("armor");

    public static final RegistryKey<Registry<ArmorTrimEffect>> ARMOR_TRIM_EFFECTS_KEY = RegistryKey.ofRegistry(CONSTRUCTOR.id("armor_trim_effects"));
    public static final Registry<ArmorTrimEffect> ARMOR_TRIM_EFFECTS = FabricRegistryBuilder.createSimple(ARMOR_TRIM_EFFECTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final RegistryEntry<EntityAttribute> GENERIC_RANGED_DAMAGE = CONSTRUCTOR.attribute("generic.ranged_damage", new ClampedEntityAttribute("attribute.beryllium.name.generic.ranged_damage", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_SWIMMING_SPEED = CONSTRUCTOR.attribute("player.swimming_speed", new ClampedEntityAttribute("attribute.beryllium.name.player.swimming_speed", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_REGENERATION = CONSTRUCTOR.attribute("generic.regeneration", new ClampedEntityAttribute("attribute.beryllium.name.generic.regeneration", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_CLEANSING = CONSTRUCTOR.attribute("generic.cleansing", new ClampedEntityAttribute("attribute.beryllium.name.generic.cleansing", 0, 0, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_SIGHT = CONSTRUCTOR.attribute("player.sight", new ClampedEntityAttribute("attribute.beryllium.name.player.sight", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_MOUNT_REGENERATION = CONSTRUCTOR.attribute("generic.mount_regeneration", new ClampedEntityAttribute("attribute.beryllium.name.generic.mount_regeneration", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_BONUS_XP = CONSTRUCTOR.attribute("player.bonus_xp", new ClampedEntityAttribute("attribute.beryllium.name.player.bonus_xp", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_CHEAP_XP = CONSTRUCTOR.attribute("player.cheap_xp", new ClampedEntityAttribute("attribute.beryllium.name.player.cheap_xp", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_MOUNT_SPEED = CONSTRUCTOR.attribute("generic.mount_speed", new ClampedEntityAttribute("attribute.beryllium.name.generic.mount_speed", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_SLIDING = CONSTRUCTOR.attribute("player.sliding", new ClampedEntityAttribute("attribute.beryllium.name.player.sliding", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_RANGED_SPEED = CONSTRUCTOR.attribute("player.ranged_speed", new ClampedEntityAttribute("attribute.beryllium.name.player.ranged_speed", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> PLAYER_DISCOUNT = CONSTRUCTOR.attribute("player.discount", new ClampedEntityAttribute("attribute.beryllium.name.player.discount", 0, -1024, 1024).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_SATURATION = CONSTRUCTOR.attribute("generic.saturation", new ClampedEntityAttribute("attribute.beryllium.name.generic.saturation", 0, -1024, 1024).setTracked(true));

    public static final ArmorTrimEffect SENTRY_EFFECT = Registry.register(ARMOR_TRIM_EFFECTS, Identifier.of("sentry"), new ArmorTrimEffect(
            List.of(new AttributeModifier(
                    GENERIC_RANGED_DAMAGE,
                    new EntityAttributeModifier(
                            CONSTRUCTOR.id("sentry"),
                            0.5,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
            )),
            List.of()
    ));
    public static final ArmorTrimEffect DUNE_EFFECT = Registry.register(ARMOR_TRIM_EFFECTS, Identifier.of("dune"), new ArmorTrimEffect(
            List.of(new AttributeModifier(
                    EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(
                            CONSTRUCTOR.id("dune"),
                            0.05,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            )),
            List.of()
    ));
    public static final ArmorTrimEffect DAMAGE_EFFECT = Registry.register(ARMOR_TRIM_EFFECTS, Identifier.of("coast"), new ArmorTrimEffect(
            List.of(new AttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(
                            CONSTRUCTOR.id("coast"),
                            1,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
            )),
            List.of()
    ));

    public static RegistryEntry<ArmorMaterial> getArmorMaterial(ItemStack stack) {
        return ((ArmorItem) stack.getItem()).getMaterial();
    }

    public static float trimMaterialScale(ItemStack stack, LivingEntity owner) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim == null) {
            return 1;
        }

        float f = switch (trim.getMaterial().getIdAsString()) {
            case "minecraft:gold" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("gold")) ? 0.5f : 1;
            case "minecraft:iron" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("iron")) ? 0.5f : 1;
            case "minecraft:diamond" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("diamond")) ? 0.5f : 1;
            case "minecraft:netherite" ->
                    getArmorMaterial(stack).matchesId(Identifier.ofVanilla("netherite")) ? 0.5f : 1;
            default -> 1;
        };

        if (owner != null) {
            for (Pair<ArmorTrim, ItemStack> trim1 : collectTrims(owner)) {
                if (!trim1.getLeft().equals(trim)) {
                    return f;
                }
            }

            f *= 1.25f;
        }

        return f;
    }

    public static float trimPatternScale(ItemStack stack, LivingEntity owner) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim == null) {
            return 1;
        }

        //if (trim.getMaterial().isIn(ENHANCE_MATERIALS)) {
        //    return 1.5f;
        //}

        return 1;
    }

    public static List<Pair<ArmorTrim, ItemStack>> collectTrims(LivingEntity entity) {
        List<Pair<ArmorTrim, ItemStack>> trims = new LinkedList<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmorSlot()) {
                ItemStack stack = entity.getEquippedStack(slot);
                ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

                if (trim != null) {
                    trims.add(new Pair<>(trim, stack));
                }
            }
        }

        return trims;
    }

    public static Optional<ArmorTrimEffect> getTrimEffect(ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim != null) {
            return ARMOR_TRIM_EFFECTS.getOrEmpty(trim.getPattern().getKey().orElseThrow().getValue());
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
        return (float) entity.getAttributeValue(GENERIC_REGENERATION);
    }

    public static float bonusCleansing(LivingEntity entity) {
        return (float) entity.getAttributeValue(GENERIC_CLEANSING);
    }

    public static float bonusSight(LivingEntity entity) {
        return (float) entity.getAttributeValue(PLAYER_SIGHT);
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

    @Override
    public void onInitialize() {
    }
}
