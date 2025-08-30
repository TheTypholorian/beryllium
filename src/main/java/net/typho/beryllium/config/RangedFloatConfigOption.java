package net.typho.beryllium.config;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class RangedFloatConfigOption extends FloatConfigOption {
    public final float min, max;

    public RangedFloatConfigOption(ItemStack icon, ConfigOptionGroup parent, String name, Float value, float min, float max) {
        super(icon, parent, name, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public void set(Object value) {
        super.set(MathHelper.clamp((float) value, min, max));
    }
}
