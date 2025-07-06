package net.typho.nemesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Nemesis implements ModInitializer {
    public static final String MOD_ID = "nemesis";

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

    public static class ThunderAspectEnchantment extends Enchantment {
        protected ThunderAspectEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
            super(weight, target, slotTypes);
        }

        @Override
        public int getMinPower(int level) {
            return 10 + 20 * (level - 1);
        }

        @Override
        public int getMaxPower(int level) {
            return super.getMinPower(level) + 50;
        }

        @Override
        public void onTargetDamaged(LivingEntity user, Entity target, int level) {
            World world = user.getWorld();

            if (!world.isClient) {
                LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                entity.setPosition(target.getX(), target.getY(), target.getZ());
                world.spawnEntity(entity);
            }
        }

        @Override
        protected boolean canAccept(Enchantment other) {
            if (other instanceof FireAspectEnchantment) {
                return false;
            }

            return super.canAccept(other);
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
                            this.random.nextTriangular(0.0, 0.0172275 * divergence),
                            this.random.nextTriangular(0.0, 0.0172275 * divergence),
                            this.random.nextTriangular(0.0, 0.0172275 * divergence)
                    )
                    .multiply(speed);
            this.setVelocity(vec3d);
            double d = vec3d.horizontalLength();
            this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float)Math.PI));
            this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 180.0F / (float)Math.PI));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
    }

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "diamond_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world);
        }
    });
    public static final Enchantment THUNDER_ASPECT = Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "thunder_aspect"), new ThunderAspectEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[0]));
    public static final EntityType<MovingEndCrystalEntity> MOVING_END_CRYSTAL_ENTITY = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "moving_end_crystal"), FabricEntityTypeBuilder.<MovingEndCrystalEntity>create(SpawnGroup.MISC, MovingEndCrystalEntity::new).dimensions(EntityDimensions.fixed(2f, 2f)).trackRangeChunks(16).trackedUpdateRate(3).build());

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW);
                });
    }
}
