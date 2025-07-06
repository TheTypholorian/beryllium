package net.typho.nemesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
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

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "diamond_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world);
        }
    });
    public static final Enchantment THUNDER_ASPECT = Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "thunder_aspect"), new ThunderAspectEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[0]));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW);
                });
    }
}
