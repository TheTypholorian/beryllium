package net.typho.beryllium.mixin.client;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    /*
    @Inject(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
            ),
            cancellable = true
    )
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel<?> model, CallbackInfo ci, @Local ItemStack stack) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim != null && trim.getPattern().isIn(Armor.INVISIBLE_TRIMS)) {
            if (Armor.trimPatternScale(stack, entity) > 1f) {
                ci.cancel();
            } else if (entity.isInvisible()) {
                ci.cancel();
            }
        }
    }
     */
}
