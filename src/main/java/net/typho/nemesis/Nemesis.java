package net.typho.nemesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Nemesis implements ModInitializer {
    public static final String MOD_ID = "nemesis";

    public static final int MAX_ENCHANTMENTS = 3;

    public static class DiamondArrowEntity extends PersistentProjectileEntity {
        public DiamondArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public DiamondArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
            super(type, x, y, z, world);
        }

        public DiamondArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
            super(type, owner, world);
        }

        {
            setDamage(6);
        }

        @Override
        public ItemStack asItemStack() {
            return new ItemStack(DIAMOND_ARROW);
        }
    }

    public static class IronArrowEntity extends PersistentProjectileEntity {
        public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
            super(type, x, y, z, world);
        }

        public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
            super(type, owner, world);
        }

        {
            setDamage(4);
        }

        @Override
        public ItemStack asItemStack() {
            return new ItemStack(IRON_ARROW);
        }
    }

    public static class FlamingArrowEntity extends PersistentProjectileEntity {
        public FlamingArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public FlamingArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
            super(type, x, y, z, world);
        }

        public FlamingArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
            super(type, owner, world);
        }

        {
            setOnFireFor(100);
        }

        @Override
        public ItemStack asItemStack() {
            return new ItemStack(FLAMING_ARROW);
        }
    }

    public static class ShockArrowEntity extends PersistentProjectileEntity {
        public ShockArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public ShockArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
            super(type, x, y, z, world);
        }

        public ShockArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
            super(type, owner, world);
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
        public ItemStack asItemStack() {
            return new ItemStack(SHOCK_ARROW);
        }
    }

    public static class MovingEndCrystalEntity extends EndCrystalEntity {
        public MovingEndCrystalEntity(EntityType<? extends EndCrystalEntity> entityType, World world) {
            super(entityType, world);
        }

        public MovingEndCrystalEntity(World world, double x, double y, double z) {
            this(MOVING_END_CRYSTAL_ENTITY, world);
            setPosition(x, y, z);
        }

        @Override
        public void tick() {
            super.tick();
            move(MovementType.SELF, getVelocity());
            setVelocity(getVelocity().subtract(0, 0.04, 0));
        }

        @Override
        public void move(MovementType movementType, Vec3d movement) {
            super.move(movementType, movement);

            if (horizontalCollision || verticalCollision) {
                if (!isRemoved() && !getWorld().isClient) {
                    remove(RemovalReason.KILLED);
                    getWorld().createExplosion(this, getDamageSources().explosion(this, null), null, this.getX(), this.getY(), this.getZ(), 6.0F, false, World.ExplosionSourceType.BLOCK);
                }
            }
        }

        public void setVelocity(double x, double y, double z, float speed, float divergence) {
            Vec3d vec3d = new Vec3d(x, y, z)
                    .normalize()
                    .add(
                            random.nextTriangular(0.0, 0.0172275 * divergence),
                            random.nextTriangular(0.0, 0.0172275 * divergence),
                            random.nextTriangular(0.0, 0.0172275 * divergence)
                    )
                    .multiply(speed);
            setVelocity(vec3d);
            double d = vec3d.horizontalLength();
            setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float)Math.PI));
            setPitch((float)(MathHelper.atan2(vec3d.y, d) * 180.0F / (float)Math.PI));
            prevYaw = getYaw();
            prevPitch = getPitch();
        }
    }

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "diamond_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world);
        }
    });
    public static final EntityType<IronArrowEntity> IRON_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "iron_arrow"), EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("iron_arrow"));
    public static final Item IRON_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "iron_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            return new IronArrowEntity(IRON_ARROW_TYPE, shooter, world);
        }
    });
    public static final EntityType<FlamingArrowEntity> FLAMING_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "flaming_arrow"), EntityType.Builder.<FlamingArrowEntity>create(FlamingArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("flaming_arrow"));
    public static final Item FLAMING_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "flaming_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            return new FlamingArrowEntity(FLAMING_ARROW_TYPE, shooter, world);
        }
    });
    public static final EntityType<ShockArrowEntity> SHOCK_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "shock_arrow"), EntityType.Builder.<ShockArrowEntity>create(ShockArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("shock_arrow"));
    public static final Item SHOCK_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "shock_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            return new ShockArrowEntity(SHOCK_ARROW_TYPE, shooter, world);
        }
    });
    public static final EntityType<MovingEndCrystalEntity> MOVING_END_CRYSTAL_ENTITY = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "moving_end_crystal"), FabricEntityTypeBuilder.<MovingEndCrystalEntity>create(SpawnGroup.MISC, MovingEndCrystalEntity::new).dimensions(EntityDimensions.fixed(2f, 2f)).trackRangeChunks(16).trackedUpdateRate(3).build());

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW, IRON_ARROW, FLAMING_ARROW, SHOCK_ARROW);
                });
    }
}
