package net.typho.beryllium.armor;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record ArmorTrimMaterialEffect(List<AttributeModifier> attributes, List<CustomTrimEffect> custom, List<RegistryKey<ArmorMaterial>> debuffed) {
    public void applyModifiers(String slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> output, ItemStack stack) {
        for (AttributeModifier attrib : attributes) {
            output.accept(attrib.attribute(), new EntityAttributeModifier(
                    attrib.modifier().id().withSuffixedPath("/" + slot.toLowerCase()),
                    attrib.modifier().value() * Armor.trimMaterialScale(stack),
                    attrib.modifier().operation()
            ));
        }
    }

    public void appendTooltip(Consumer<Text> output, @Nullable PlayerEntity player, ItemStack stack) {
        if (!attributes.isEmpty() || !custom.isEmpty()) {
            output.accept(ScreenTexts.EMPTY);
            output.accept(Text.translatable("item.beryllium.modifiers.trim_material").formatted(Formatting.GRAY));

            for (AttributeModifier attribute : attributes) {
                stack.appendAttributeModifierTooltip(output, player, attribute.attribute(), attribute.modifier());
            }

            for (CustomTrimEffect custom : custom) {
                custom.appendTooltip(output, stack);
            }
        }
    }
}
