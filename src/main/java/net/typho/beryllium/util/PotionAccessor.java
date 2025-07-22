package net.typho.beryllium.util;

import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public interface PotionAccessor {
    void setEffects(List<StatusEffectInstance> effects);
}
