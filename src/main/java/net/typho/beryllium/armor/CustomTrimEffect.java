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

    default boolean shouldRender(boolean invisible, ItemStack stack, @Nullable ArmorTrim trim) {
        return true;
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

        if (!shouldRender(false, stack, trim)) {
            output.accept(ScreenTexts.space().append(Text.translatable("trim_effect.beryllium.name.invisible_always")).formatted(Formatting.BLUE));
        } else if (!shouldRender(true, stack, trim)) {
            output.accept(ScreenTexts.space().append(Text.translatable("trim_effect.beryllium.name.invisible")).formatted(Formatting.BLUE));
        }
    }

    default void appendTooltipValue(Consumer<Text> output, float d, EntityAttributeModifier.Operation op, Text name) {
        double e;

        if (op == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE || op == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            e = d * 100;
        } else {
            e = d;
        }

        if (d > 0.0) {
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

    class Silence implements CustomTrimEffect {
        @Override
        public boolean shouldRender(boolean invisible, ItemStack stack, @Nullable ArmorTrim trim) {
            if (invisible) {
                return false;
            }

            return Armor.trimPatternScale(stack) < 1.001;
        }
    }
}
