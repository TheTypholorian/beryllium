package net.typho.beryllium.mixin.exploring;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.typho.beryllium.exploring.Exploring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "createPlayerAttributes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(Exploring.PLAYER_AIR_MINING_EFFICIENCY));
    }

    @ModifyConstant(
            method = "getBlockBreakingSpeed",
            constant = @Constant(floatValue = 5)
    )
    private float stableFooting(float constant) {
        return 1 / (float) getAttributeValue(Exploring.PLAYER_AIR_MINING_EFFICIENCY);
    }
}
