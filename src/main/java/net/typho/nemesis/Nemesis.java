package net.typho.nemesis;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Nemesis implements ModInitializer {
    public static final String MOD_ID = "nemesis";

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

    public static class CopperArrowEntity extends PersistentProjectileEntity {
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
            return new ItemStack(COPPER_ARROW);
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

    public static class GlaiveItem extends SwordItem implements CustomPoseItem, DualModelItem {
        public GlaiveItem(ToolMaterial toolMaterial, Settings settings) {
            super(toolMaterial, settings);
        }

        @Override
        public BipedEntityModel.ArmPose pose() {
            return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
        }

        @Override
        public Identifier worldModel() {
            Identifier id = Registries.ITEM.getId(this);
            return Identifier.of(id.getNamespace(), id.getPath() + "_3d");
        }

        @Override
        public Identifier guiModel() {
            return Registries.ITEM.getId(this);
        }

        public static AttributeModifiersComponent glaiveModifiers(double range, ToolMaterial material, float damage, float speed) {
            AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
            builder.add(
                            EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                            new EntityAttributeModifier(Identifier.of(MOD_ID, "entity_interaction_range"), range, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    )
                    .add(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, damage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    )
                    .add(
                            EntityAttributes.GENERIC_ATTACK_SPEED,
                            new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    );
            return builder.build();
        }
    }

    public static class ScytheItem extends SwordItem implements DualModelItem {
        public ScytheItem(ToolMaterial toolMaterial, Settings settings) {
            super(toolMaterial, settings);
        }

        @Override
        public Identifier worldModel() {
            Identifier id = Registries.ITEM.getId(this);
            return Identifier.of(id.getNamespace(), id.getPath() + "_3d");
        }

        @Override
        public Identifier guiModel() {
            return Registries.ITEM.getId(this);
        }

        public static AttributeModifiersComponent scytheModifiers(ToolMaterial material, float damage, float speed) {
            AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
            builder.add(
                            EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                            new EntityAttributeModifier(Identifier.of(MOD_ID, "entity_interaction_range"), -1, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    )
                    .add(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, damage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    )
                    .add(
                            EntityAttributes.GENERIC_ATTACK_SPEED,
                            new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    )
                    .add(
                            EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
                            new EntityAttributeModifier(Identifier.of(MOD_ID, "attack_knockback"), -5, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    );
            return builder.build();
        }
    }

    public interface CustomPoseItem {
        BipedEntityModel.ArmPose pose();
    }

    public interface DualModelItem {
        Identifier worldModel();

        Identifier guiModel();
    }

    public interface SweepingItem {
        float sweep(PlayerEntity player, ItemStack stack);
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
    public static final EntityType<CopperArrowEntity> COPPER_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "copper_arrow"), EntityType.Builder.<CopperArrowEntity>create(CopperArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("copper_arrow"));
    public static final Item COPPER_ARROW = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "copper_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new CopperArrowEntity(COPPER_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            CopperArrowEntity arrowEntity = new CopperArrowEntity(COPPER_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<EndCrystalProjectileEntity> END_CRYSTAL_PROJECTILE_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, "moving_end_crystal"), EntityType.Builder.<EndCrystalProjectileEntity>create(EndCrystalProjectileEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).maxTrackingRange(256).trackingTickInterval(3).build());
    public static final Item NETHERITE_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "netherite_glaive"),
            new GlaiveItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.NETHERITE, 3, -3.2f)))
    );
    public static final Item DIAMOND_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "diamond_glaive"),
            new GlaiveItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.DIAMOND, 3, -3.2f)))
    );
    public static final Item IRON_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "iron_glaive"),
            new GlaiveItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.IRON, 3, -3.2f)))
    );
    public static final Item GOLDEN_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "golden_glaive"),
            new GlaiveItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.GOLD, 3, -3.2f)))
    );
    public static final Item NETHERITE_SCYTHE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "netherite_scythe"),
            new ScytheItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(ToolMaterials.NETHERITE, 4, -3.4f)))
    );
    public static final RegistryEntry<EntityAttribute> GENERIC_HORIZONTAL_DRAG = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "horizontal_drag"), new ClampedEntityAttribute("attribute.nemesis.name.generic.horizontal_drag", 0.91, 0, 1).setTracked(true));
    //public static final RegistryEntry<EntityAttribute> GENERIC_VERTICAL_DRAG = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "vertical_drag"), new ClampedEntityAttribute("attribute.nemesis.name.generic.vertical_drag", 0.098, 0, 1));

    public static String toRomanNumeral(int n) {
        enum Letter {
            M(1000), D(500), C(100), L(50), X(10), V(5), I(1);

            public final int value;

            Letter(int value) {
                this.value = value;
            }
        }

        StringBuilder builder = new StringBuilder();

        for (Letter letter : Letter.values()) {
            int num = Math.floorDiv(n, letter.value);

            switch (num) {
                case 1, 2, 3: {
                    builder.append(letter.name().repeat(num));
                    break;
                }
                case 4: {
                    if (letter == Letter.M) {
                        builder.append("MMMM");
                    } else {
                        builder.append(letter.name()).append(Letter.values()[letter.ordinal() - 1]);
                    }
                    break;
                }
            }

            n -= letter.value * num;
        }

        return builder.toString();
    }

    public static float getMaxEnchantmentPoints(ItemStack stack) {
        return stack.getItem().getEnchantability() / 5f;
    }

    public static float getEnchantmentPoints(ItemStack stack) {
        return getEnchantmentPoints(EnchantmentHelper.getEnchantments(stack));
    }

    public static float getEnchantmentPoints(ItemEnchantmentsComponent enchants) {
        float i = 0;

        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : enchants.getEnchantmentEntries()) {
            i += getEnchantmentPoints(entry.getKey().value());
        }

        return i;
    }

    public static float getEnchantmentPoints(Enchantment enchant) {
        return 1f / enchant.getWeight();
    }

    public static ItemStack getRecipeStack(RegistryEntry<Enchantment> enchant, int level) {
        return new ItemStack(Items.SNOWBALL, level);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW, IRON_ARROW, FLAMING_ARROW, COPPER_ARROW);
                    entries.addAfter(Items.NETHERITE_SWORD, NETHERITE_GLAIVE, NETHERITE_SCYTHE);
                    entries.addAfter(Items.DIAMOND_SWORD, DIAMOND_GLAIVE);
                    entries.addAfter(Items.IRON_SWORD, IRON_GLAIVE);
                    entries.addAfter(Items.GOLDEN_SWORD, GOLDEN_GLAIVE);
                });
    }
}
