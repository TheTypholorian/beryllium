package net.typho.nemesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Nemesis implements ModInitializer {
    public static final String MOD_ID = "nemesis";

    public static class DiamondArrowEntity extends ArrowEntity {
        public DiamondArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
            super(entityType, world);
            setDamage(6);
        }

        @Override
        public ItemStack asItemStack() {
            return new ItemStack(DIAMOND_ARROW);
        }
    }

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier(MOD_ID, "diamond_arrow"), EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "diamond_arrow"), new ArrowItem(new FabricItemSettings()) {
        @Override
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
            ArrowEntity arrow = new DiamondArrowEntity(DIAMOND_ARROW_TYPE, world);
            arrow.setOwner(shooter);
            arrow.setPos(shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());
            arrow.initFromStack(stack);
            arrow.setDamage(6);
            return arrow;
        }
    });

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.addAfter(Items.ARROW, DIAMOND_ARROW);
                });
    }
}
