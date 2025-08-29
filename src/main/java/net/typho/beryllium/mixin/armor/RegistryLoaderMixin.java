package net.typho.beryllium.mixin.armor;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.*;
import net.minecraft.resource.ResourceManager;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @Inject(
            method = "loadFromResource(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/registry/RegistryLoader;parseAndAdd(Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Lnet/minecraft/registry/RegistryOps;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/resource/Resource;Lnet/minecraft/registry/entry/RegistryEntryInfo;)V"
            ),
            cancellable = true
    )
    private static <E> void loadFromResource(ResourceManager resourceManager, RegistryOps.RegistryInfoGetter infoGetter, MutableRegistry<E> registry, Decoder<E> elementDecoder, Map<RegistryKey<?>, Exception> errors, CallbackInfo ci, @Local RegistryKey<E> resKey) {
        System.out.println("Load " + resKey);
        if (ServerConfig.DURABILITY_REMOVAL.get() && registry.getKey().equals(RegistryKeys.ENCHANTMENT) && ServerConfig.DURABILITY_ENCHANTS.contains(resKey.getValue())) {
            System.out.println("cancel");
            //ci.cancel();
        }
    }
}
