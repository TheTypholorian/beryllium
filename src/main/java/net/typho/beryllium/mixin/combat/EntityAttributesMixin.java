package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Inject(
            method = "register",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void register(String id, EntityAttribute attribute, CallbackInfoReturnable<RegistryEntry<EntityAttribute>> cir) {
        if (id.equals("generic.attack_knockback")) {
            cir.setReturnValue(Registry.registerReference(Registries.ATTRIBUTE, Identifier.ofVanilla(id), new ClampedEntityAttribute("attribute.name.generic.attack_knockback", 0, -1000, 1000)));
        }
    }
}
