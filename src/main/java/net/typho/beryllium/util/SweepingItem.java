package net.typho.beryllium.util;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.typho.beryllium.config.Config;

public interface SweepingItem {
    default float sweep(PlayerEntity player, ItemStack stack) {
        return (float) player.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE) * Config.sweepMarginMultiplier.get();
    }
}
