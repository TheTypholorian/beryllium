package net.typho.beryllium.mixin.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    @Shadow
    @Final
    private static TrackedData<Byte> LOYALTY;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    @Inject(
            method = "onEntityHit",
            at = @At("TAIL")
    )
    private void onEntityHit(EntityHitResult hit, CallbackInfo ci) {
        if (getOwner() != null) {
            int reeling = EnchantmentHelper.getLevel(
                    getWorld()
                            .getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(Beryllium.COMBAT.id("reeling"))
                            .orElseThrow(),
                    getItemStack()
            );

            if (reeling > 0) {
                byte loyalty = dataTracker.get(LOYALTY);

                if (loyalty > 0) {
                    hit.getEntity().addVelocity(getOwner().getPos().subtract(getPos()).normalize().multiply(reeling * Beryllium.CONFIG.combat.reelingMultiplierLoyal));
                    hit.getEntity().velocityModified = true;
                } else {
                    getOwner().addVelocity(getPos().subtract(getOwner().getPos()).normalize().multiply(reeling * Beryllium.CONFIG.combat.reelingMultiplierNotLoyal));
                    getOwner().velocityModified = true;
                }
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult hit) {
        if (getOwner() != null) {
            int reeling = EnchantmentHelper.getLevel(
                    getWorld()
                            .getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(Beryllium.COMBAT.id("reeling"))
                            .orElseThrow(),
                    getItemStack()
            );

            if (reeling > 0) {
                byte loyalty = dataTracker.get(LOYALTY);

                if (loyalty <= 0) {
                    getOwner().addVelocity(getPos().subtract(getOwner().getPos()).normalize().multiply(reeling * Beryllium.CONFIG.combat.reelingMultiplierNotLoyal));
                    getOwner().velocityModified = true;
                }
            }
        }
    }
}
