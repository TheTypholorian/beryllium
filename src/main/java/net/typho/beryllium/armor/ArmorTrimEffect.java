package net.typho.beryllium.armor;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.function.BiConsumer;

public record ArmorTrimEffect(List<AttributeModifier> attributes, List<RegistryKey<ArmorMaterial>> debuffed) {
    public void applyModifiers(String slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> output) {
        for (AttributeModifier attrib : attributes) {
            output.accept(attrib.attribute(), new EntityAttributeModifier(
                    attrib.modifier().id().withSuffixedPath("/" + slot.toLowerCase()),
                    attrib.modifier().value(),
                    attrib.modifier().operation()
            ));
        }
    }
}
