package net.typho.beryllium.mixin.armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.typho.beryllium.armor.Armor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Shadow
    @Nullable
    public abstract LivingEntity getControllingPassenger();

    @WrapOperation(
            method = "getNearbySlipperiness",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getSlipperiness()F"
            )
    )
    private float getSlipperiness(Block instance, Operation<Float> original) {
        float f = original.call(instance);
        LivingEntity controller = getControllingPassenger();

        if (controller != null) {
            f *= (float) controller.getAttributeValue(Armor.GENERIC_BLOCK_SLIPPERINESS);
        }

        return Math.min(1, f);
    }
}
