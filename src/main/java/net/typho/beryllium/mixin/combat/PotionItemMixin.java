package net.typho.beryllium.mixin.combat;

import net.minecraft.item.Item;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V"
            ),
            index = 0
    )
    private static Item.Settings applyMaxCount(Item.Settings settings) {
        return settings.maxCount(16);
    }
}