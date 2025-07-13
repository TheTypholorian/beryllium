package net.typho.beryllium;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface SweepingItem {
    float sweep(PlayerEntity player, ItemStack stack);
}
