package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.typho.beryllium.util.SweepingItem;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
@Implements(@Interface(iface = SweepingItem.class, prefix = "sweep$"))
public class SwordItemMixin {
    public float sweep$sweep(PlayerEntity player, ItemStack stack) {
        return (float) player.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE) / 10;
    }
}
