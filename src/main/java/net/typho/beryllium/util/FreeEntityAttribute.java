package net.typho.beryllium.util;

import net.minecraft.entity.attribute.EntityAttribute;

public class FreeEntityAttribute extends EntityAttribute {
    public FreeEntityAttribute(String translationKey, double fallback) {
        super(translationKey, fallback);
    }
}
