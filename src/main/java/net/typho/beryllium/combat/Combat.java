package net.typho.beryllium.combat;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.Module;
import org.jetbrains.annotations.Nullable;

public class Combat extends Module {
    public final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, id("diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, id("diamond_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            DiamondArrowEntity arrowEntity = new DiamondArrowEntity(DIAMOND_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public final EntityType<IronArrowEntity> IRON_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, id("iron_arrow"), EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("iron_arrow"));
    public final Item IRON_ARROW = Registry.register(Registries.ITEM, id("iron_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new IronArrowEntity(IRON_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            IronArrowEntity arrowEntity = new IronArrowEntity(IRON_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public final EntityType<FlamingArrowEntity> FLAMING_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, id("flaming_arrow"), EntityType.Builder.<FlamingArrowEntity>create(FlamingArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("flaming_arrow"));
    public final Item FLAMING_ARROW = Registry.register(Registries.ITEM, id("flaming_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new FlamingArrowEntity(FLAMING_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            FlamingArrowEntity arrowEntity = new FlamingArrowEntity(FLAMING_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public final EntityType<CopperArrowEntity> COPPER_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, id("copper_arrow"), EntityType.Builder.<CopperArrowEntity>create(CopperArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("copper_arrow"));
    public final Item COPPER_ARROW = Registry.register(Registries.ITEM, id("copper_arrow"), new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new CopperArrowEntity(COPPER_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            CopperArrowEntity arrowEntity = new CopperArrowEntity(COPPER_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public final EntityType<EndCrystalProjectileEntity> END_CRYSTAL_PROJECTILE_ENTITY = Registry.register(Registries.ENTITY_TYPE, id("moving_end_crystal"), EntityType.Builder.<EndCrystalProjectileEntity>create(EndCrystalProjectileEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).maxTrackingRange(256).trackingTickInterval(3).build());
    public final Item NETHERITE_GLAIVE = Registry.register(
            Registries.ITEM,
            id("netherite_glaive"),
            new GlaiveItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(this, 3, ToolMaterials.NETHERITE, 2, -3.2f)))
    );
    public final Item DIAMOND_GLAIVE = Registry.register(
            Registries.ITEM,
            id("diamond_glaive"),
            new GlaiveItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(this, 3, ToolMaterials.DIAMOND, 2, -3.2f)))
    );
    public final Item IRON_GLAIVE = Registry.register(
            Registries.ITEM,
            id("iron_glaive"),
            new GlaiveItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(this, 3, ToolMaterials.IRON, 2, -3.2f)))
    );
    public final Item GOLDEN_GLAIVE = Registry.register(
            Registries.ITEM,
            id("golden_glaive"),
            new GlaiveItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(this, 3, ToolMaterials.GOLD, 2, -3.2f)))
    );
    public final Item NETHERITE_SCYTHE = Registry.register(
            Registries.ITEM,
            id("netherite_scythe"),
            new ScytheItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(this, ToolMaterials.NETHERITE, 4, -3.4f)))
    );
    public final Item DIAMOND_SCYTHE = Registry.register(
            Registries.ITEM,
            id("diamond_scythe"),
            new ScytheItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(this, ToolMaterials.DIAMOND, 4, -3.4f)))
    );
    public final Item IRON_SCYTHE = Registry.register(
            Registries.ITEM,
            id("iron_scythe"),
            new ScytheItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(this, ToolMaterials.IRON, 4, -3.4f)))
    );
    public final Item GOLDEN_SCYTHE = Registry.register(
            Registries.ITEM,
            id("golden_scythe"),
            new ScytheItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(this, ToolMaterials.GOLD, 4, -3.4f)))
    );
    public final ComponentType<Float> SHIELD_DURABILITY = component("shield_damage", builder -> builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT));

    public Combat(String name) {
        super(name);
    }

    public float shieldDurability(ItemStack shield) {
        return shield.getOrDefault(SHIELD_DURABILITY, Beryllium.CONFIG.combat.shieldMaxDurability).floatValue();
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

    public static class Config extends ConfigSection {
        public int enderPearlCooldown = 300;
        public float enderPearlSpeed = 1;
        public int endCrystalCooldown = 30;
        public float endCrystalPower = 4;
        public boolean maceRebalance = true;
        public boolean sweepingMargin = true;
        public float sweepMarginMultiplier = 0.05f;
        public boolean crossbowEndCrystals = true;
        public boolean respawnAnchorsDontExplode = true;
        public boolean shieldDurability = true;
        public int shieldMaxDurability = 30;
        public int shieldLowerCooldown = 60;
        public int splashPotionCooldown = 100;
    }
}
