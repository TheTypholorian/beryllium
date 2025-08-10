package net.typho.beryllium.combat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.client.EndCrystalProjectileEntityRenderer;
import net.typho.beryllium.util.Constructor;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class Combat implements ModInitializer, ClientModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("combat");

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW_TYPE = CONSTRUCTOR.entity("diamond_arrow", EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("diamond_arrow"));
    public static final Item DIAMOND_ARROW = CONSTRUCTOR.item("diamond_arrow", new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new DiamondArrowEntity(DIAMOND_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            DiamondArrowEntity arrowEntity = new DiamondArrowEntity(DIAMOND_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<IronArrowEntity> IRON_ARROW_TYPE = CONSTRUCTOR.entity("iron_arrow", EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("iron_arrow"));
    public static final Item IRON_ARROW = CONSTRUCTOR.item("iron_arrow", new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new IronArrowEntity(IRON_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            IronArrowEntity arrowEntity = new IronArrowEntity(IRON_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<FlamingArrowEntity> FLAMING_ARROW_TYPE = CONSTRUCTOR.entity("flaming_arrow", EntityType.Builder.<FlamingArrowEntity>create(FlamingArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("flaming_arrow"));
    public static final Item FLAMING_ARROW = CONSTRUCTOR.item("flaming_arrow", new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new FlamingArrowEntity(FLAMING_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            FlamingArrowEntity arrowEntity = new FlamingArrowEntity(FLAMING_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<CopperArrowEntity> COPPER_ARROW_TYPE = CONSTRUCTOR.entity("copper_arrow", EntityType.Builder.<CopperArrowEntity>create(CopperArrowEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("copper_arrow"));
    public static final Item COPPER_ARROW = CONSTRUCTOR.item("copper_arrow", new ArrowItem(new Item.Settings()) {
        public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
            return new CopperArrowEntity(COPPER_ARROW_TYPE, shooter, world, stack.copyWithCount(1), shotFrom);
        }

        public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
            CopperArrowEntity arrowEntity = new CopperArrowEntity(COPPER_ARROW_TYPE, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
            arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrowEntity;
        }
    });
    public static final EntityType<EndCrystalProjectileEntity> END_CRYSTAL_PROJECTILE_ENTITY = CONSTRUCTOR.entity("moving_end_crystal", EntityType.Builder.<EndCrystalProjectileEntity>create(EndCrystalProjectileEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).maxTrackingRange(256).trackingTickInterval(3).build());
    public static final Item NETHERITE_GLAIVE = CONSTRUCTOR.item("netherite_glaive",
            new GlaiveItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(CONSTRUCTOR, 3, ToolMaterials.NETHERITE, 2, -3.2f)))
    );
    public static final Item DIAMOND_GLAIVE = CONSTRUCTOR.item("diamond_glaive",
            new GlaiveItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(CONSTRUCTOR, 3, ToolMaterials.DIAMOND, 2, -3.2f)))
    );
    public static final Item IRON_GLAIVE = CONSTRUCTOR.item("iron_glaive",
            new GlaiveItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(CONSTRUCTOR, 3, ToolMaterials.IRON, 2, -3.2f)))
    );
    public static final Item GOLDEN_GLAIVE = CONSTRUCTOR.item("golden_glaive",
            new GlaiveItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(GlaiveItem.glaiveModifiers(CONSTRUCTOR, 3, ToolMaterials.GOLD, 2, -3.2f)))
    );
    public static final Item NETHERITE_SCYTHE = CONSTRUCTOR.item("netherite_scythe",
            new ScytheItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(CONSTRUCTOR, ToolMaterials.NETHERITE, 4, -3.4f)))
    );
    public static final Item DIAMOND_SCYTHE = CONSTRUCTOR.item("diamond_scythe",
            new ScytheItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(CONSTRUCTOR, ToolMaterials.DIAMOND, 4, -3.4f)))
    );
    public static final Item IRON_SCYTHE = CONSTRUCTOR.item("iron_scythe",
            new ScytheItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(CONSTRUCTOR, ToolMaterials.IRON, 4, -3.4f)))
    );
    public static final Item GOLDEN_SCYTHE = CONSTRUCTOR.item("golden_scythe",
            new ScytheItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(ScytheItem.scytheModifiers(CONSTRUCTOR, ToolMaterials.GOLD, 4, -3.4f)))
    );
    public static final ComponentType<Float> SHIELD_DURABILITY = CONSTRUCTOR.dataComponent("shield_damage", builder -> builder.codec(Codecs.POSITIVE_FLOAT).packetCodec(PacketCodecs.FLOAT));
    public static final Block POTION_CAULDRON = CONSTRUCTOR.block("potion_cauldron", new PotionCauldronBlock(Biome.Precipitation.NONE, AbstractBlock.Settings.create()));
    public static final BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON_BLOCK_ENTITY = CONSTRUCTOR.blockEntity("potion_cauldron", BlockEntityType.Builder.create(PotionCauldronBlockEntity::new, POTION_CAULDRON));

    public static final TagKey<ArmorTrimPattern> RANGED_DAMAGE_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("ranged_damage"));
    public static final TagKey<ArmorTrimPattern> SPEED_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("speed"));
    public static final TagKey<ArmorTrimPattern> DAMAGE_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("damage"));
    public static final TagKey<ArmorTrimPattern> SWIMMING_SPEED_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("swimming_speed"));
    public static final TagKey<ArmorTrimPattern> REGEN_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("regen"));
    public static final TagKey<ArmorTrimPattern> ARMOR_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("armor"));
    public static final TagKey<ArmorTrimPattern> ATTACK_SPEED_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("attack_speed"));
    public static final TagKey<ArmorTrimPattern> FORTIFY_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("fortify"));
    public static final TagKey<ArmorTrimPattern> FARM_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("farm"));
    public static final TagKey<ArmorTrimPattern> SIGHT_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("sight"));
    public static final TagKey<ArmorTrimPattern> LUCK_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("luck"));
    public static final TagKey<ArmorTrimPattern> MOUNT_REGEN_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("mount_regen"));
    public static final TagKey<ArmorTrimPattern> MORE_XP_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("more_xp"));
    public static final TagKey<ArmorTrimPattern> CHEAP_XP_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("cheap_xp"));
    public static final TagKey<ArmorTrimPattern> MOUNT_SPEED_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("mount_speed"));
    public static final TagKey<ArmorTrimPattern> INVISIBLE_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("invisible"));

    public static final TagKey<ArmorTrimMaterial> SWEEPING_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("sweeping"));
    public static final TagKey<ArmorTrimMaterial> REACH_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("reach"));
    public static final TagKey<ArmorTrimMaterial> ENHANCE_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("enhance"));
    public static final TagKey<ArmorTrimMaterial> DISCOUNT_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("discount"));
    public static final TagKey<ArmorTrimMaterial> ENCHANTABILITY_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("enchantability"));
    public static final TagKey<ArmorTrimMaterial> KNOCKBACK_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("knockback"));
    public static final TagKey<ArmorTrimMaterial> JUMP_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("jump"));
    public static final TagKey<ArmorTrimMaterial> EFFICIENCY_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("efficiency"));
    public static final TagKey<ArmorTrimMaterial> ARMOR_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("armor"));
    public static final TagKey<ArmorTrimMaterial> SATURATION_MATERIALS = TagKey.of(RegistryKeys.TRIM_MATERIAL, CONSTRUCTOR.id("saturation"));

    public static final TagKey<Item> HORSE_ARMOR = TagKey.of(RegistryKeys.ITEM, CONSTRUCTOR.id("horse_armor"));

    public static float shieldDurability(ItemStack shield) {
        return shield.getOrDefault(SHIELD_DURABILITY, Beryllium.SERVER_CONFIG.shieldMaxDurability.get()).floatValue();
    }

    public static RegistryEntry<ArmorMaterial> getArmorMaterial(ItemStack stack) {
        return ((ArmorItem) stack.getItem()).getMaterial();
    }

    public static float trimMaterialScale(ItemStack stack, LivingEntity owner) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim == null) {
            return 1;
        }

        float f = switch (trim.getMaterial().getIdAsString()) {
            case "minecraft:gold" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("gold")) ? 0.5f : 1;
            case "minecraft:iron" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("iron")) ? 0.5f : 1;
            case "minecraft:diamond" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("diamond")) ? 0.5f : 1;
            case "minecraft:netherite" -> getArmorMaterial(stack).matchesId(Identifier.ofVanilla("netherite")) ? 0.5f : 1;
            default -> 1;
        };

        if (owner != null) {
            for (Pair<ArmorTrim, ItemStack> trim1 : collectTrims(owner)) {
                if (!trim1.getLeft().equals(trim)) {
                    return f;
                }
            }

            f *= 1.25f;
        }

        return f;
    }

    public static float trimPatternScale(ItemStack stack, LivingEntity owner) {
        ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

        if (trim == null) {
            return 1;
        }

        if (trim.getMaterial().isIn(ENHANCE_MATERIALS)) {
            return 1.5f;
        }

        return 1;
    }

    public static List<Pair<ArmorTrim, ItemStack>> collectTrims(LivingEntity entity) {
        List<Pair<ArmorTrim, ItemStack>> trims = new LinkedList<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmorSlot()) {
                ItemStack stack = entity.getEquippedStack(slot);
                ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

                if (trim != null) {
                    trims.add(new Pair<>(trim, stack));
                }
            }
        }

        return trims;
    }

    public static float bonusRangedDamage(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getPattern().isIn(RANGED_DAMAGE_TRIMS)) {
                f += 0.5f * trimPatternScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusSpeed(LivingEntity entity) {
        float f = 0;

            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
                if (trim.getLeft().getPattern().isIn(SPEED_TRIMS)) {
                    f += 0.05f * trimPatternScale(trim.getRight(), entity);
                }
            }

        return f;
    }

    public static float bonusMeleeDamage(LivingEntity entity) {
        float f = 0;

            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
                if (trim.getLeft().getPattern().isIn(DAMAGE_TRIMS)) {
                    f += trimPatternScale(trim.getRight(), entity);
                }
            }

        return f;
    }

    public static float bonusSwimmingSpeed(LivingEntity entity) {
        float f = 0;

            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
                if (trim.getLeft().getPattern().isIn(SWIMMING_SPEED_TRIMS)) {
                    f += 0.02f * trimPatternScale(trim.getRight(), entity);
                }
            }

        return f;
    }

    public static int bonusArmor(LivingEntity entity) {
        float f = 0;

            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
                if (trim.getLeft().getPattern().isIn(ARMOR_TRIMS)) {
                    f += 2 * trimPatternScale(trim.getRight(), entity);
                }
            }

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(ARMOR_MATERIALS)) {
                f += trimMaterialScale(trim.getRight(), entity);
            }
        }

        return (int) f;
    }

    public static float bonusAttackSpeed(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getPattern().isIn(ATTACK_SPEED_TRIMS)) {
                f += 0.05f * trimPatternScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusHealing(LivingEntity entity) {
        float f = 0;

            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
                if (trim.getLeft().getPattern().isIn(REGEN_TRIMS)) {
                    f += 0.02f * trimPatternScale(trim.getRight(), entity);
                }
            }

        return f;
    }

    public static float bonusKnockbackResistance(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getPattern().isIn(FORTIFY_TRIMS)) {
                f += 0.1f * trimPatternScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusSight(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getPattern().isIn(SIGHT_TRIMS)) {
                f += trimPatternScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusSweepingRatio(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(SWEEPING_MATERIALS)) {
                f += 0.1f * trimMaterialScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusReach(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(REACH_MATERIALS)) {
                f += 0.5f * trimMaterialScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static int bonusReputation(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(DISCOUNT_MATERIALS)) {
                f += 5 * trimMaterialScale(trim.getRight(), entity);
            }
        }

        return (int) f;
    }

    public static float bonusAttackKnockback(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(KNOCKBACK_MATERIALS)) {
                f += 0.1f * trimMaterialScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusJumpHeight(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(JUMP_MATERIALS)) {
                f += 0.1f * trimMaterialScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static float bonusMiningEfficiency(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(EFFICIENCY_MATERIALS)) {
                f += 8f * trimMaterialScale(trim.getRight(), entity);
            }
        }

        return f;
    }

    public static int bonusSaturation(LivingEntity entity) {
        float f = 0;

        for (Pair<ArmorTrim, ItemStack> trim : collectTrims(entity)) {
            if (trim.getLeft().getMaterial().isIn(SATURATION_MATERIALS)) {
                f += trimMaterialScale(trim.getRight(), entity);
            }
        }

        return (int) f;
    }

    public static float bonusMountSpeed(Entity entity) {
        float f = 0;

        LivingEntity passenger = entity.getControllingPassenger();

        if (passenger != null) {
            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(passenger)) {
                if (trim.getLeft().getPattern().isIn(MOUNT_SPEED_TRIMS)) {
                    f += 0.05f * trimPatternScale(trim.getRight(), passenger);
                }
            }
        }

        if (entity instanceof LivingEntity living) {
            for (Pair<ArmorTrim, ItemStack> trim : collectTrims(living)) {
                if (trim.getLeft().getPattern().isIn(MOUNT_SPEED_TRIMS)) {
                    f += 0.05f * trimPatternScale(trim.getRight(), living);
                }
            }
        }

        return f;
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

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Combat.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(DiamondArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/diamond_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.IRON_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(IronArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/iron_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.FLAMING_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(FlamingArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/flaming_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.COPPER_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(CopperArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/copper_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.END_CRYSTAL_PROJECTILE_ENTITY, EndCrystalProjectileEntityRenderer::new);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> view != null && view.getBlockEntityRenderData(pos) instanceof Integer color ? color : -1, Combat.POTION_CAULDRON);
    }
}
