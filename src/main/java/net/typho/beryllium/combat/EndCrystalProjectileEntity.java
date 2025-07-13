package net.typho.beryllium.combat;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EndCrystalProjectileEntity extends PersistentProjectileEntity {
    public EndCrystalProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public EndCrystalProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    public EndCrystalProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    public void explode() {
        if (!isRemoved() && !getWorld().isClient) {
            remove(RemovalReason.KILLED);

            getWorld().createExplosion(this, getDamageSources().explosion(this, getOwner()), null, getX(), getY(), getZ(), 3f, false, World.ExplosionSourceType.BLOCK);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() != getOwner()) {
            explode();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        explode();
    }

    @Override
    public void tick() {
        super.tick();

        if (age >= 100) {
            explode();
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.END_CRYSTAL);
    }
}
