package net.typho.beryllium.mixin.pillagers;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.ModItemComponents;
import net.typho.beryllium.pillagers.RavagerShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    public abstract void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int color);

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
            )
    )
    private void renderItemDecorations(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        int charge = stack.getOrDefault(ModItemComponents.ravagerShieldCharge.get(), 0);

        if (charge > 0) {
            int width = Mth.clamp((int) ((float) charge / RavagerShieldItem.MAX_CHARGE * 13), 0, 13);
            int color = 0xFF93AEAD;
            int barX = x + 2;
            int barY = y + 1;
            fill(RenderType.guiOverlay(), barX, barY, barX + 13, barY + 2, 0xFF000000);
            fill(RenderType.guiOverlay(), barX, barY, barX + width, barY + 1, color);
        }
    }
}
