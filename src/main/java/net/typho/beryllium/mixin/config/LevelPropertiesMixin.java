package net.typho.beryllium.mixin.config;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
@SuppressWarnings("deprecation")
public class LevelPropertiesMixin {
    @Inject(
            method = "readProperties",
            at = @At("RETURN")
    )
    private static <T> void readProperties(Dynamic<T> dynamic, LevelInfo info, LevelProperties.SpecialProperty specialProperty, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> cir) {
        Config.read(dynamic.get(Beryllium.MOD_ID + "ServerConfig").orElseEmptyMap());
    }

    @Inject(
            method = "updateProperties",
            at = @At("HEAD")
    )
    private void updateProperties(DynamicRegistryManager registryManager, NbtCompound levelNbt, NbtCompound playerNbt, CallbackInfo ci) {
        levelNbt.put(Beryllium.MOD_ID + "ServerConfig", Config.write(registryManager));
    }
}
