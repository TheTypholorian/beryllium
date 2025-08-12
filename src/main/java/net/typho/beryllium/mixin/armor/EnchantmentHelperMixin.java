package net.typho.beryllium.mixin.armor;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(
            method = "applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
            at = @At("TAIL")
    )
    private static void applyAttributeModifiers(ItemStack stack, EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> output, CallbackInfo ci) {
        if (slot.isArmorSlot()) {
            Armor.getTrimEffect(stack).ifPresent(effect -> effect.applyModifiers(slot.name(), output, stack));
        }
    }

    @Inject(
            method = "applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V",
            at = @At("TAIL")
    )
    private static void applyAttributeModifiers(ItemStack stack, AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> output, CallbackInfo ci) {
        if (stack.getItem() instanceof Equipment equipment ? slot.matches(equipment.getSlotType()) && slot.ordinal() >= 4 && slot.ordinal() <= 7 : slot == AttributeModifierSlot.ARMOR) {
            Armor.getTrimEffect(stack).ifPresent(effect -> effect.applyModifiers(slot.name(), output, stack));
        }
    }
}
