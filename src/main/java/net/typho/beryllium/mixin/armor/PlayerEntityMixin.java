package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    protected HungerManager hungerManager;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "createPlayerAttributes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(Armor.PLAYER_SWIMMING_SPEED)
                .add(Armor.PLAYER_GAMMA)
                .add(Armor.PLAYER_XP_GAIN)
                .add(Armor.PLAYER_XP_COST)
                .add(Armor.PLAYER_SLIDING)
                .add(Armor.PLAYER_RANGED_SPEED)
                .add(Armor.PLAYER_DISCOUNT));
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/HungerManager;update(Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private void bonusRegen(CallbackInfo ci) {
        heal(Armor.bonusHealing(this));

        if (age % 20 == 0) {
            hungerManager.add(Armor.bonusSaturation(this), 0.1f);
        }
    }
}
