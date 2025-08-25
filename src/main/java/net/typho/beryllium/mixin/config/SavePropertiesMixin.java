package net.typho.beryllium.mixin.config;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.world.SaveProperties;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SaveProperties.class)
public interface SavePropertiesMixin {
    @ModifyReturnValue(
            method = "getEnabledFeatures",
            at = @At("TAIL")
    )
    private FeatureSet getEnabledFeatures(FeatureSet original) {
        return ServerConfig.getEnabledFeatures(original);
    }
}
