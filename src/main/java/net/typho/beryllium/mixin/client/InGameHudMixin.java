package net.typho.beryllium.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.util.Arm;
import net.typho.beryllium.config.BerylliumConfig;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Nullable
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Inject(
            method = "renderHotbar",
            at = @At("TAIL")
    )
    private void renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (BerylliumConfig.HUD_ITEM_TOOLTIPS.get()) {
            PlayerEntity player = getCameraPlayer();

            if (player != null) {
                ItemStack main = player.getMainHandStack();

                if (!main.isEmpty()) {
                    drawTooltip(context, main, Arm.RIGHT, context.getScaledWindowWidth() / 2 + (player.getMainArm() == Arm.RIGHT ? 127 : -127), context.getScaledWindowHeight(), player, getTextRenderer());
                }

                ItemStack off = player.getOffHandStack();

                if (!off.isEmpty()) {
                    drawTooltip(context, off, Arm.LEFT, context.getScaledWindowWidth() / 2 + (player.getMainArm() == Arm.LEFT ? 127 : -127), context.getScaledWindowHeight(), player, getTextRenderer());
                }
            }
        }
    }

    @Inject(
            method = "renderHeldItemTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
        if (BerylliumConfig.HUD_ITEM_TOOLTIPS.get()) {
            ci.cancel();
        }
    }

    @Unique
    private static void drawTooltip(DrawContext context, ItemStack stack, Arm arm, int x, int y, PlayerEntity player, TextRenderer renderer) {
        context.drawTooltip(
                renderer,
                stack.getTooltip(Item.TooltipContext.DEFAULT, player, TooltipType.BASIC)
                        .stream()
                        .flatMap(t -> renderer.wrapLines(t, 160).stream())
                        .toList(),
                (screenWidth, screenHeight, x1, y1, width1, height1) -> new Vector2i(
                        player.getMainArm() != arm ? x1 - width1 : x1,
                        y1 - height1 - 8
                ),
                x,
                y
        );
    }
}
