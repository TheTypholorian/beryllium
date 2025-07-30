package net.typho.beryllium.mixin.enchanting;

import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AnimalArmorItem.class)
public class AnimalArmorItemMixin {
    /**
     * @author The Typhothanian
     * @reason Enchantable horse armor
     */
    @Overwrite
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
