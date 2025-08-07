package net.typho.beryllium.mixin.combat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.typho.beryllium.combat.Combat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow protected HungerManager hungerManager;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "getMovementSpeed()F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void bonusSpeed(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() + Combat.bonusSpeed((PlayerEntity) (Object) this));
    }

    @ModifyVariable(
            method = "attack",
            at = @At(value = "STORE", ordinal = 0)
    )
    private float bonusDamage(float f) {
        return f + Combat.bonusMeleeDamage((PlayerEntity) (Object) this);
    }

    @WrapOperation(
            method = "getAttackCooldownProgressPerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
            )
    )
    private double bonusDamage(PlayerEntity instance, RegistryEntry<EntityAttribute> registryEntry, Operation<Double> original) {
        return original.call(instance, registryEntry) + Combat.bonusAttackSpeed(instance);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/HungerManager;update(Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private void bonusRegen(CallbackInfo ci) {
        heal(Combat.bonusHealing(this));

        if (age % 20 == 0) {
            hungerManager.add(Combat.bonusSaturation(this), 0.1f);
        }
    }

    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D",
                    ordinal = 1
            )
    )
    private double bonusSweeping(PlayerEntity instance, RegistryEntry<EntityAttribute> registryEntry, Operation<Double> original) {
        return Math.min(1, original.call(instance, registryEntry) + Combat.bonusSweepingRatio(instance));
    }

    @WrapOperation(
            method = {
                    "getBlockInteractionRange",
                    "getEntityInteractionRange"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
            )
    )
    private double bonusBlockReach(PlayerEntity instance, RegistryEntry<EntityAttribute> registryEntry, Operation<Double> original) {
        return original.call(instance, registryEntry) + Combat.bonusReach(instance);
    }

    @WrapOperation(
            method = "getBlockBreakingSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D",
                    ordinal = 0
            )
    )
    private double bonusMiningEfficiency(PlayerEntity instance, RegistryEntry<EntityAttribute> registryEntry, Operation<Double> original) {
        return original.call(instance, registryEntry) + Combat.bonusMiningEfficiency(instance);
    }
}
