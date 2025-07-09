package net.typho.nemesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Nemesis implements ModInitializer {
    public static final String MOD_ID = "nemesis";

    public static final int MAX_ENCHANTMENTS = 3;

    public static class DiamondArrowEntity extends PersistentProjectileEntity {
        public DiamondArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public DiamondArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        public DiamondArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
            super(type, owner, world, stack, shotFrom);
        }

        {
            setDamage(6);
        }

        @Override
        protected ItemStack getDefaultItemStack() {
            return new ItemStack(DIAMOND_ARROW);
        }
    }

    public static class IronArrowEntity extends PersistentProjectileEntity {
        public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
            super(type, owner, world, stack, shotFrom);
        }

        {
            setDamage(4);
        }

        @Override
        protected ItemStack getDefaultItemStack() {
            return new ItemStack(IRON_ARROW);
        }
    }

    public static class FlamingArrowEntity extends PersistentProjectileEntity {
        public FlamingArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public FlamingArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        public FlamingArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
            super(type, owner, world, stack, shotFrom);
        }

        {
            setOnFireFor(100);
        }

        @Override
        protected ItemStack getDefaultItemStack() {
            return new ItemStack(FLAMING_ARROW);
        }
    }

    public static class ShockArrowEntity extends PersistentProjectileEntity {
        public ShockArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        public ShockArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        public ShockArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
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
            return new ItemStack(SHOCK_ARROW);
        }
    }

    public static class EndCrystalProjectileEntity extends PersistentProjectileEntity {
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
                remove(Entity.RemovalReason.KILLED);

                getWorld().createExplosion(this, getDamageSources().explosion(this, getOwner()), null, getX(), getY(), getZ(), 4f, true, World.ExplosionSourceType.BLOCK);
            }
        }

        @Override
        protected void onEntityHit(EntityHitResult entityHitResult) {
            super.onEntityHit(entityHitResult);

            if (entityHitResult.getEntity() != getOwner()) {
                explode();
            }
        }

        @Override
        protected void onBlockHit(BlockHitResult blockHitResult) {
            super.onBlockHit(blockHitResult);
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

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "diamond_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            DiamondArrowEntity arrowEntity = new DiamondArrowEntity(DIAMOND_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<IronArrowEntity> IRON_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "iron_arrow"), EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("iron_arrow"));
    public static final Item IRON_ARROW = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "iron_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new IronArrowEntity(IRON_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            IronArrowEntity arrowEntity = new IronArrowEntity(IRON_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<FlamingArrowEntity> FLAMING_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "flaming_arrow"), EntityType.Builder.<FlamingArrowEntity>create(FlamingArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("flaming_arrow"));
    public static final Item FLAMING_ARROW = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "flaming_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new FlamingArrowEntity(FLAMING_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            FlamingArrowEntity arrowEntity = new FlamingArrowEntity(FLAMING_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<ShockArrowEntity> SHOCK_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "shock_arrow"), EntityType.Builder.<ShockArrowEntity>create(ShockArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("shock_arrow"));
    public static final Item SHOCK_ARROW = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "shock_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new ShockArrowEntity(SHOCK_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            ShockArrowEntity arrowEntity = new ShockArrowEntity(SHOCK_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<EndCrystalProjectileEntity> END_CRYSTAL_PROJECTILE_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "moving_end_crystal"), EntityType.Builder.<EndCrystalProjectileEntity>create(EndCrystalProjectileEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).maxTrackingRange(256).trackingTickInterval(3).build());
    public static final RegistryKey<Enchantment> VITALITY_ENCHANTMENT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "vitality"));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW, IRON_ARROW, FLAMING_ARROW, SHOCK_ARROW);
                });
        /*
        Registry.register(
                RegistryKeys.ENCHANTMENT,
                VITALITY_ENCHANTMENT,
                Enchantment.builder(
                                Enchantment.definition(
                                        En.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                        10,
                                        4,
                                        Enchantment.leveledCost(1, 11),
                                        Enchantment.leveledCost(12, 11),
                                        1,
                                        AttributeModifierSlot.ARMOR
                                )
                        )
                        .exclusiveSet(registryEntryLookup2.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE_SET))
                        .addEffect(
                                EnchantmentEffectComponentTypes.DAMAGE_PROTECTION,
                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.0F)),
                                DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().tag(TagPredicate.unexpected(DamageTypeTags.BYPASSES_INVULNERABILITY)))
                        )
        );
         */
    }
}
