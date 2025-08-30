package net.typho.beryllium.mixin.armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.typho.beryllium.config.BerylliumConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(AttributeModifiersComponent.class)
public class AttributeModifiersComponentMixin {
    @WrapOperation(
            method = {
                    "applyModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
                    "applyModifiers(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V"
            )
    )
    private void disabledArmor(BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> instance, Object attribute, Object modifier, Operation<Void> original) {
        if (!(BerylliumConfig.DISABLED_ARMOR.get() && attribute == EntityAttributes.GENERIC_ARMOR)) {
            original.call(instance, attribute, modifier);
        }
    }
}
