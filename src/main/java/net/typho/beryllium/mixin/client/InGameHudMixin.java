package net.typho.beryllium.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.typho.beryllium.client.BerylliumClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
        PlayerEntity player = getCameraPlayer();

        if (player != null) {
            ItemStack main = player.getMainHandStack();

            if (!main.isEmpty()) {
                BerylliumClient.drawTooltip(context, main, Arm.RIGHT, context.getScaledWindowWidth() / 2 + (player.getMainArm() == Arm.RIGHT ? 127 : -127), context.getScaledWindowHeight(), player, getTextRenderer());
            }

            ItemStack off = player.getOffHandStack();

            if (!off.isEmpty()) {
                BerylliumClient.drawTooltip(context, off, Arm.LEFT, context.getScaledWindowWidth() / 2 + (player.getMainArm() == Arm.LEFT ? 127 : -127), context.getScaledWindowHeight(), player, getTextRenderer());
            }
        }
    }
}
