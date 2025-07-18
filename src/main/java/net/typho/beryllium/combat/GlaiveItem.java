package net.typho.beryllium.combat;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.util.CustomPoseItem;
import net.typho.beryllium.util.DualModelItem;

public class GlaiveItem extends SwordItem implements CustomPoseItem, DualModelItem {
    public GlaiveItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public BipedEntityModel.ArmPose pose() {
        return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
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

    @Override
    public void transform(ModelTransformationMode renderMode, BakedModel model, MatrixStack matrices) {
        if (!(renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED)) {
            matrices.scale(1, 2, 2);
        }
    }

    public static AttributeModifiersComponent glaiveModifiers(double range, ToolMaterial material, float damage, float speed) {
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        builder.add(
                        EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new EntityAttributeModifier(Identifier.of(Beryllium.MOD_ID, "entity_interaction_range"), range, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, damage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                );
        return builder.build();
    }
}
