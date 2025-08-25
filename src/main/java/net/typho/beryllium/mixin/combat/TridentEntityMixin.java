package net.typho.beryllium.mixin.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.exploring.Exploring;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    @Shadow
    public abstract ItemStack getWeaponStack();

    @Shadow
    @Final
    private static TrackedData<Byte> LOYALTY;

    @Unique
    private float ownerDistance = -1;

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL")
    )
    private void init(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        initReeling();
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL")
    )
    private void init(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
        initReeling();
    }

    @Inject(
            method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V",
            at = @At("TAIL")
    )
    private void init(EntityType<?> entityType, World world, CallbackInfo ci) {
        initReeling();
    }

    @Unique
    private void initReeling() {
        if (!getWorld().isClient) {
            getComponent(Exploring.REELING).setReeling(EnchantmentHelper.getLevel(
                    getWorld()
                            .getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(Beryllium.EXPLORING_CONSTRUCTOR.id("reeling"))
                            .orElseThrow(),
                    getItemStack()
            ) / 16f);
        }
    }

    @Inject(
            method = "age",
            at = @At("HEAD")
    )
    public void age(CallbackInfo ci) {
        if (getOwner() != null && getWeaponStack() != null) {
            float reeling = getComponent(Exploring.REELING).getReeling();

            if (reeling > 0) {
                getOwner().addVelocity(getPos().subtract(getOwner().getPos()).normalize().multiply(reeling));
                getOwner().velocityModified = true;

                float d = (float) getPos().squaredDistanceTo(getOwner().getPos());

                if (ownerDistance >= 0) {
                    if (d > (ownerDistance * 1.1f)) {
                        dataTracker.set(LOYALTY, (byte) 16);
                    }
                }

                ownerDistance = d;

                if (age >= 200) {
                    dataTracker.set(LOYALTY, (byte) 16);
                }
            }
        }
    }
}
