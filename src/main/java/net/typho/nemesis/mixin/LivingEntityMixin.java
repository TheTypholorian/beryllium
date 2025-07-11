package net.typho.nemesis.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyConstant(
            method = "travel",
            constant = @Constant(floatValue = 0.91f)
    )
    private float getCooldown(float constant) {
        return 0.95f;
    }
}
