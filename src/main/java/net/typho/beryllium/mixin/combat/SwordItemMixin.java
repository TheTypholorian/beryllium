package net.typho.beryllium.mixin.combat;

import net.minecraft.item.SwordItem;
import net.typho.beryllium.util.SweepingItem;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
@Implements(@Interface(iface = SweepingItem.class, prefix = "sweep$"))
public class SwordItemMixin {
}
