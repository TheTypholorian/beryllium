package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
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
                .add(Armor.GENERIC_SATURATION));
    }
}
