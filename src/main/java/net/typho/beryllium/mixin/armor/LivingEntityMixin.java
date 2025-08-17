package net.typho.beryllium.mixin.armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyVariable(
            method = "travel",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private float bonusSwimmingSpeed(float f) {
        if ((Object) this instanceof PlayerEntity) {
            f += Armor.bonusSwimmingSpeed((LivingEntity) (Object) this);
        }

        return f;
    }

    @Inject(
            method = "createLivingAttributes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(Armor.GENERIC_RANGED_DAMAGE)
                .add(Armor.GENERIC_REGENERATION)
                .add(Armor.GENERIC_CLEANSING)
                .add(Armor.GENERIC_MOUNT_REGENERATION)
                .add(Armor.GENERIC_MOUNT_SPEED)
                .add(Armor.GENERIC_SATURATION)
                .add(Armor.GENERIC_BLOCK_SLIPPERINESS)
                .add(Armor.GENERIC_RANGED_SPEED));
    }

    @WrapOperation(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getSlipperiness()F"
            )
    )
    private float getSlipperiness(Block instance, Operation<Float> original) {
        return Math.min(1, original.call(instance) * (float) getAttributeValue(Armor.GENERIC_BLOCK_SLIPPERINESS));
    }
}
