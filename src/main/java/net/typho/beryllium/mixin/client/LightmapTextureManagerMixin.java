package net.typho.beryllium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.typho.beryllium.armor.Armor;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @WrapOperation(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Double;floatValue()F",
                    ordinal = 1
            )
    )
    private float gamma(Double instance, Operation<Float> original) {
        return ServerConfig.ultraDark.get() ? Armor.bonusSight(MinecraftClient.getInstance().player) * (1 - ServerConfig.ultraDarkBlend(MinecraftClient.getInstance().world)) : original.call(instance) + Armor.bonusSight(MinecraftClient.getInstance().player);
    }
}
