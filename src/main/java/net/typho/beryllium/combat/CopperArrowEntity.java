package net.typho.beryllium.combat;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CopperArrowEntity extends PersistentProjectileEntity {
    public CopperArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CopperArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    public CopperArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    public void thunder(World world) {
        if (world.isThundering()) {
            LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            entity.setPosition(getPos());
            world.spawnEntity(entity);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        thunder(getWorld());
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        thunder(getWorld());
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Combat.COPPER_ARROW);
    }
}
