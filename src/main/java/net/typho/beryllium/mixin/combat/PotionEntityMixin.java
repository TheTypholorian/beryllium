package net.typho.beryllium.mixin.combat;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity implements FlyingItemEntity {
    @Shadow protected abstract void applyWater();

    @Shadow protected abstract boolean isLingering();

    @Shadow protected abstract void applyLingeringPotion(PotionContentsComponent potion);

    @Shadow protected abstract void applySplashPotion(Iterable<StatusEffectInstance> effects, @Nullable Entity entity);

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    /**
     * @author The Typhothanian
     * @reason Wet effect
     */
    @Overwrite
    public void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!getWorld().isClient) {
            ItemStack itemStack = getStack();
            PotionContentsComponent contents = itemStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);

            if (contents.matches(Potions.WATER)) {
                applyWater();

                if (contents.hasEffects()) {
                    if (isLingering()) {
                        applyLingeringPotion(contents);
                    } else {
                        applySplashPotion(
                                contents.getEffects(), hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null
                        );
                    }
                }
            }

            int i = contents.potion().isPresent() && ((Potion)((RegistryEntry<?>)contents.potion().get()).value()).hasInstantEffect()
                    ? WorldEvents.INSTANT_SPLASH_POTION_SPLASHED
                    : WorldEvents.SPLASH_POTION_SPLASHED;
            getWorld().syncWorldEvent(i, getBlockPos(), contents.getColor());
            discard();
        }
    }
}
