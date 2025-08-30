package net.typho.beryllium.config;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class RangedIntConfigOption extends IntConfigOption {
    public final int min, max;

    public RangedIntConfigOption(ItemStack icon, ConfigOptionGroup parent, String name, Integer value, int min, int max) {
        super(icon, parent, name, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public void set(Object value) {
        super.set(MathHelper.clamp((int) value, min, max));
    }
}
