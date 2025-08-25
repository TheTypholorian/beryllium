package net.typho.beryllium.mixin.config;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureManager;
import net.typho.beryllium.config.FeatureProperty;
import net.typho.beryllium.config.Property;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureFlags.class)
public class FeatureFlagsMixin {
    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/featuretoggle/FeatureManager$Builder;addVanillaFlag(Ljava/lang/String;)Lnet/minecraft/resource/featuretoggle/FeatureFlag;"
            )
    )
    private static void clinit(CallbackInfo ci, @Local FeatureManager.Builder builder) {
        for (Property<?> property : ServerConfig.properties.values()) {
            if (property instanceof FeatureProperty feature && !builder.featureFlags.containsKey(feature.id)) {
                feature.flag = builder.addFlag(feature.id);
            }
        }
    }
}
