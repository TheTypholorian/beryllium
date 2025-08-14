package net.typho.beryllium.mixin.client;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.render.BackgroundRenderer$BlindnessFogModifier")
public class BlindnessFogModifierMixin {
    @Inject(
            method = "applyStartEndModifier",
            at = @At("TAIL")
    )
    private void fog(BackgroundRenderer.FogData fogData, LivingEntity entity, StatusEffectInstance effect, float viewDistance, float tickDelta, CallbackInfo ci) {
        if (ServerConfig.ultraDark.get()) {
            fogData.fogStart += 8;
            fogData.fogEnd += 16;
        }
    }
}
