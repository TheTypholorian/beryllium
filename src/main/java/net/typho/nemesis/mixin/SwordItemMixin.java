package net.typho.nemesis.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
@Implements(@Interface(iface = Nemesis.SweepingItem.class, prefix = "sweep$"))
public class SwordItemMixin {
    public float sweep$sweep(PlayerEntity player, ItemStack stack) {
        return (float) player.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE) / 5;
    }
}
