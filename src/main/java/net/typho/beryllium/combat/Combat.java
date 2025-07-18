package net.typho.beryllium.combat;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.Module;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class Combat implements Module {
    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Beryllium.MOD_ID, "diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, Identifier.of(Beryllium.MOD_ID, "diamond_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            DiamondArrowEntity arrowEntity = new DiamondArrowEntity(DIAMOND_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<IronArrowEntity> IRON_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Beryllium.MOD_ID, "iron_arrow"), EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("iron_arrow"));
    public static final Item IRON_ARROW = Registry.register(Registries.ITEM, Identifier.of(Beryllium.MOD_ID, "iron_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new IronArrowEntity(IRON_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            IronArrowEntity arrowEntity = new IronArrowEntity(IRON_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<FlamingArrowEntity> FLAMING_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Beryllium.MOD_ID, "flaming_arrow"), EntityType.Builder.<FlamingArrowEntity>create(FlamingArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("flaming_arrow"));
    public static final Item FLAMING_ARROW = Registry.register(Registries.ITEM, Identifier.of(Beryllium.MOD_ID, "flaming_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new FlamingArrowEntity(FLAMING_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            FlamingArrowEntity arrowEntity = new FlamingArrowEntity(FLAMING_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<CopperArrowEntity> COPPER_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Beryllium.MOD_ID, "copper_arrow"), EntityType.Builder.<CopperArrowEntity>create(CopperArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("copper_arrow"));
    public static final Item COPPER_ARROW = Registry.register(Registries.ITEM, Identifier.of(Beryllium.MOD_ID, "copper_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new CopperArrowEntity(COPPER_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            CopperArrowEntity arrowEntity = new CopperArrowEntity(COPPER_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<EndCrystalProjectileEntity> END_CRYSTAL_PROJECTILE_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Beryllium.MOD_ID, "moving_end_crystal"), EntityType.Builder.<EndCrystalProjectileEntity>create(EndCrystalProjectileEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).maxTrackingRange(256).trackingTickInterval(3).build());
    public static final Item NETHERITE_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "netherite_glaive"),
            new GlaiveItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.NETHERITE, 2, -3.4f)))
    );
    public static final Item DIAMOND_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "diamond_glaive"),
            new GlaiveItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.DIAMOND, 2, -3.4f)))
    );
    public static final Item IRON_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "iron_glaive"),
            new GlaiveItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.IRON, 2, -3.4f)))
    );
    public static final Item GOLDEN_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "golden_glaive"),
            new GlaiveItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.GOLD, 2, -3.4f)))
    );
    public static final Item NETHERITE_SCYTHE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "netherite_scythe"),
            new ScytheItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(ToolMaterials.NETHERITE, 4, -3.4f)))
    );
    public static final Item DIAMOND_SCYTHE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "diamond_scythe"),
            new ScytheItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(ToolMaterials.DIAMOND, 4, -3.4f)))
    );
    public static final Item IRON_SCYTHE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "iron_scythe"),
            new ScytheItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(ToolMaterials.IRON, 4, -3.4f)))
    );
    public static final Item GOLDEN_SCYTHE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "golden_scythe"),
            new ScytheItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(ToolMaterials.GOLD, 4, -3.4f)))
    );
    public static final RegistryEntry<StatusEffect> WET_EFFECT = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Beryllium.MOD_ID, "wet"), new StatusEffect(StatusEffectCategory.BENEFICIAL, 0x38BDE6) {
    });
    public static final RegistryKey<Enchantment> DASH_ENCHANTMENT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Beryllium.MOD_ID, "dash"));
    public static final RegistryKey<Enchantment> REEL_ENCHANTMENT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Beryllium.MOD_ID, "reel"));

    public static @Nullable EntityHitResult raycast(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double maxDistance, double margin) {
        World world = entity.getWorld();
        double distance = maxDistance;
        Entity found = null;
        Vec3d foundPos = null;

        for (Entity entity3 : world.getOtherEntities(entity, box, predicate)) {
            Box box2 = entity3.getBoundingBox().expand(entity3.getTargetingMargin() + margin);
            Optional<Vec3d> optional = box2.raycast(min, max);
            if (box2.contains(min)) {
                if (distance >= 0.0) {
                    found = entity3;
                    foundPos = optional.orElse(min);
                    distance = 0.0;
                }
            } else if (optional.isPresent()) {
                Vec3d vec3d2 = optional.get();
                double e = min.squaredDistanceTo(vec3d2);
                if (e < distance || distance == 0.0) {
                    if (entity3.getRootVehicle() == entity.getRootVehicle()) {
                        if (distance == 0.0) {
                            found = entity3;
                            foundPos = vec3d2;
                        }
                    } else {
                        found = entity3;
                        foundPos = vec3d2;
                        distance = e;
                    }
                }
            }
        }

        return found == null ? null : new EntityHitResult(found, foundPos);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW, IRON_ARROW, FLAMING_ARROW, COPPER_ARROW);
                    entries.addAfter(Items.NETHERITE_SWORD, NETHERITE_GLAIVE, NETHERITE_SCYTHE);
                    entries.addAfter(Items.DIAMOND_SWORD, DIAMOND_GLAIVE, DIAMOND_SCYTHE);
                    entries.addAfter(Items.IRON_SWORD, IRON_GLAIVE, IRON_SCYTHE);
                    entries.addAfter(Items.GOLDEN_SWORD, GOLDEN_GLAIVE, GOLDEN_SCYTHE);
                });
    }
}
