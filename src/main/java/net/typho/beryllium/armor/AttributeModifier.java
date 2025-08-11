package net.typho.beryllium.armor;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

public record AttributeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
}
