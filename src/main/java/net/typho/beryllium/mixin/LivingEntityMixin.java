package net.typho.beryllium.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "hasStatusEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hasStatusEffect(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (ServerConfig.ultraDarkBlend(getWorld()) > 0.5f && effect == StatusEffects.BLINDNESS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "getStatusEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getStatusEffect(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<StatusEffectInstance> cir) {
        if (ServerConfig.ultraDarkBlend(getWorld()) > 0.5f && effect == StatusEffects.BLINDNESS) {
            cir.setReturnValue(new StatusEffectInstance(effect, -1, 0, true, false, false));
        }
    }
}
