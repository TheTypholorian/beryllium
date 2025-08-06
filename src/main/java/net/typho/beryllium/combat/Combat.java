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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
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

    public static final TagKey<Biome> DUNE_BIOMES = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("dunes"));
    public static final TagKey<Biome> COLD_OCEAN_BIOMES = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("cold_oceans"));
    public static final TagKey<Biome> WARM_OCEAN_BIOMES = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("warm_oceans"));

    public static final TagKey<ArmorTrimPattern> RANGED_DAMAGE_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("ranged_damage"));
    public static final TagKey<ArmorTrimPattern> SAND_VITALITY_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("sand_vitality"));
    public static final TagKey<ArmorTrimPattern> OCEAN_DAMAGE_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("ocean_damage"));
    public static final TagKey<ArmorTrimPattern> OCEAN_SPEED_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("ocean_speed"));
    public static final TagKey<ArmorTrimPattern> JUNGLE_REGEN_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("jungle_regen"));
    public static final TagKey<ArmorTrimPattern> GROUND_ARMOR_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("ground_armor"));
    public static final TagKey<ArmorTrimPattern> ATTACK_SPEED_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("attack_speed"));
    public static final TagKey<ArmorTrimPattern> FORTIFY_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("fortify"));
    public static final TagKey<ArmorTrimPattern> GOLD_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("gold"));
    public static final TagKey<ArmorTrimPattern> SIGHT_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("sight"));
    public static final TagKey<ArmorTrimPattern> INVISIBLE_TRIMS = TagKey.of(RegistryKeys.TRIM_PATTERN, CONSTRUCTOR.id("invisible"));

    public static float shieldDurability(ItemStack shield) {
        return shield.getOrDefault(SHIELD_DURABILITY, Beryllium.SERVER_CONFIG.shieldMaxDurability.get()).floatValue();
    }

    public static List<ArmorTrim> collectTrims(LivingEntity entity) {
        List<ArmorTrim> trims = new LinkedList<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmorSlot()) {
                ItemStack stack = entity.getEquippedStack(slot);
                ArmorTrim trim = stack.getOrDefault(DataComponentTypes.TRIM, null);

                if (trim != null) {
                    trims.add(trim);
                }
            }
        }

        return trims;
    }

    public static float bonusRangedDamage(LivingEntity entity) {
        float f = 0;

        for (ArmorTrim trim : collectTrims(entity)) {
            if (trim.getPattern().isIn(RANGED_DAMAGE_TRIMS)) {
                f += 0.5f;
            }
        }

        return f;
    }

    public static float bonusSpeed(LivingEntity entity) {
        float f = 0;

        if (entity.getWorld().getBiome(entity.getBlockPos()).isIn(DUNE_BIOMES)) {
            for (ArmorTrim trim : collectTrims(entity)) {
                if (trim.getPattern().isIn(SAND_VITALITY_TRIMS)) {
                    f += 0.1f;
                }
            }
        }

        return f;
    }

    public static float bonusMeleeDamage(LivingEntity entity) {
        float f = 0;

        if (entity.getWorld().getBiome(entity.getBlockPos()).isIn(COLD_OCEAN_BIOMES)) {
            for (ArmorTrim trim : collectTrims(entity)) {
                if (trim.getPattern().isIn(OCEAN_DAMAGE_TRIMS)) {
                    f++;
                }
            }
        }

        return f;
    }

    public static float bonusSwimmingSpeed(LivingEntity entity) {
        float f = 0;

        if (entity.getWorld().getBiome(entity.getBlockPos()).isIn(WARM_OCEAN_BIOMES)) {
            for (ArmorTrim trim : collectTrims(entity)) {
                if (trim.getPattern().isIn(OCEAN_SPEED_TRIMS)) {
                    f++;
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
