package net.typho.beryllium.mixin.tooltips;

import net.minecraft.world.item.Item;
import net.typho.beryllium.tooltips.ItemIsMaterialStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class ItemMixin implements ItemIsMaterialStorage {
    @Unique
    private boolean beryllium$isMaterial = false;

    @Override
    public boolean beryllium$isMaterial() {
        return beryllium$isMaterial;
    }

    @Override
    public void beryllium$setIsMaterial(boolean value) {
        beryllium$isMaterial = value;
    }
}
