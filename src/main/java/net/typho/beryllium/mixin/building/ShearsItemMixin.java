package net.typho.beryllium.mixin.building;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V"
            ),
            index = 0
    )
    private static Item.Settings applyRecipeRemainder(Item.Settings settings) {
        return settings.recipeRemainder(Items.SHEARS);
    }
}