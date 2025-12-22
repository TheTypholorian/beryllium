package net.typho.beryllium.mixin.tooltips;

import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilMixin {
    @ModifyConstant(
            method = "renderTooltipBackground",
            constant = {
                    @Constant(intValue = -267386864),
                    @Constant(intValue = 1347420415),
                    @Constant(intValue = 1344798847)
            }
    )
    private static int renderTooltipBackground(int constant) {
        return constant | 0xFF000000;
    }
}
