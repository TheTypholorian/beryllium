package net.typho.beryllium.mixin.enchanting;

import net.minecraft.block.EnchantingTableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 1)
    )
    private static int height(int constant) {
        return constant + 1;
    }
}
