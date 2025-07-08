package net.typho.nemesis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Enchantments.class)
public class EnchantmentsMixin {
    @Inject(
            method = "register",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder, CallbackInfo ci) {
        Identifier id = key.getValue();

        if (Objects.equals(id.getNamespace(), Identifier.DEFAULT_NAMESPACE)) {
            switch (id.getPath()) {
                case "unbreaking", "mending", "flame": {
                    ci.cancel();
                    break;
                }
            }
        }
    }
}
