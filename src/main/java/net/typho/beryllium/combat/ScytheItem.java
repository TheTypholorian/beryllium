package net.typho.beryllium.combat;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.util.DualModelItem;
import net.typho.beryllium.util.SweepingItem;

public class ScytheItem extends SwordItem implements DualModelItem, SweepingItem {
    public ScytheItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public Identifier worldModel() {
        Identifier id = Registries.ITEM.getId(this);
        return Identifier.of(id.getNamespace(), id.getPath() + "_3d");
    }

    @Override
    public Identifier guiModel() {
        return Registries.ITEM.getId(this);
    }

    public static AttributeModifiersComponent scytheModifiers(ToolMaterial material, float damage, float speed) {
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        builder.add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, damage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
                        new EntityAttributeModifier(Identifier.of(Beryllium.MOD_ID, "attack_knockback"), -5, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                );
        return builder.build();
    }

    @Override
    public float sweep(PlayerEntity player, ItemStack stack) {
        return 1;
    }
}
