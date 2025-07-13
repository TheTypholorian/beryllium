package net.typho.beryllium.combat;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;

public final class Combat {
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
            new GlaiveItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.NETHERITE, 3, -3.2f)))
    );
    public static final Item DIAMOND_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "diamond_glaive"),
            new GlaiveItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.DIAMOND, 3, -3.2f)))
    );
    public static final Item IRON_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "iron_glaive"),
            new GlaiveItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.IRON, 3, -3.2f)))
    );
    public static final Item GOLDEN_GLAIVE = Registry.register(
            Registries.ITEM,
            Identifier.of(Beryllium.MOD_ID, "golden_glaive"),
            new GlaiveItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(3, ToolMaterials.GOLD, 3, -3.2f)))
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

    private Combat() {
    }

    public static void init() {
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
