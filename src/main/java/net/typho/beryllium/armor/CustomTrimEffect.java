package net.typho.beryllium.armor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;

public interface CustomTrimEffect {
    default float trimMaterialScale(ItemStack stack, ArmorTrim trim) {
        return 1;
    }

    default float trimPatternScale(ItemStack stack, ArmorTrim trim) {
        return 1;
    }

    default int bonusEnchantmentCapacity(ItemStack stack, ArmorTrim trim) {
        return 0;
    }

    class Diamond implements CustomTrimEffect {
        @Override
        public float trimPatternScale(ItemStack stack, ArmorTrim trim) {
            return 1.5f;
        }
    }

    class Gold implements CustomTrimEffect {
        @Override
        public int bonusEnchantmentCapacity(ItemStack stack, ArmorTrim trim) {
            return 4;
        }
    }
}
