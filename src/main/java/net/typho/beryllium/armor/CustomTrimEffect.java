package net.typho.beryllium.armor;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface CustomTrimEffect {
    default float trimMaterialScale(ItemStack stack, @Nullable ArmorTrim trim) {
        return 1;
    }

    default float trimPatternScale(ItemStack stack, @Nullable ArmorTrim trim) {
        return 1;
    }

    default int bonusEnchantmentCapacity(ItemStack stack, @Nullable ArmorTrim trim) {
        return 0;
    }

    default void appendTooltip(Consumer<Text> output, ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);
        float mScale = trimMaterialScale(stack, trim);

        if (mScale != 1) {
            appendTooltipValue(output, mScale - 1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE, Text.translatable("trim_effect.beryllium.name.material_scale"));
        }

        float pScale = trimPatternScale(stack, trim);

        if (pScale != 1) {
            appendTooltipValue(output, pScale - 1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE, Text.translatable("trim_effect.beryllium.name.pattern_scale"));
        }

        int ench = bonusEnchantmentCapacity(stack, trim);

        if (ench != 1) {
            appendTooltipValue(output, ench, EntityAttributeModifier.Operation.ADD_VALUE, Text.translatable("trim_effect.beryllium.name.enchantment_capacity"));
        }
    }

    default void appendTooltipValue(Consumer<Text> output, float d, EntityAttributeModifier.Operation op, Text name) {
        boolean bl = false;

        double e;

        if (op == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE || op == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            e = d * 100;
        } else {
            e = d;
        }

        if (bl) {
            output.accept(
                    ScreenTexts.space()
                            .append(
                                    Text.translatable(
                                            "attribute.modifier.equals." + op.getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            name
                                    )
                            )
                            .formatted(Formatting.DARK_GREEN)
            );
        } else if (d > 0.0) {
            output.accept(
                    Text.translatable(
                                    "attribute.modifier.plus." + op.getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                    name
                            )
                            .formatted(Formatting.BLUE)
            );
        } else if (d < 0.0) {
            output.accept(
                    Text.translatable(
                                    "attribute.modifier.take." + op.getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(-e),
                                    name
                            )
                            .formatted(Formatting.BLUE)
            );
        }
    }

    class Diamond implements CustomTrimEffect {
        @Override
        public float trimPatternScale(ItemStack stack, @Nullable ArmorTrim trim) {
            return 1.5f;
        }
    }

    class Gold implements CustomTrimEffect {
        @Override
        public int bonusEnchantmentCapacity(ItemStack stack, @Nullable ArmorTrim trim) {
            return 4;
        }
    }
}
