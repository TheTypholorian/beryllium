package net.typho.beryllium.exploring;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.ComponentType;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GrassColors;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.BasaltColumnsFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.client.FireflyFactory;
import net.typho.beryllium.combat.ReelingComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import terrablender.api.EndBiomeRegistry;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

import java.awt.*;
import java.util.Optional;

public class Exploring implements ModInitializer, ClientModInitializer, EntityComponentInitializer, TerraBlenderApi {
    public static final LootFunctionType<ExplorationCompassLootFunction> EXPLORATION_COMPASS = Registry.register(Registries.LOOT_FUNCTION_TYPE, Beryllium.EXPLORING_CONSTRUCTOR.id("exploration_compass"), new LootFunctionType<>(ExplorationCompassLootFunction.CODEC));

    public static final ComponentType<DyeColor> COMPASS_NEEDLE_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, Beryllium.EXPLORING_CONSTRUCTOR.id("needle_color"), ComponentType.<DyeColor>builder().codec(DyeColor.CODEC).build());

    public static final SimpleParticleType FIREFLY_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, Beryllium.EXPLORING_CONSTRUCTOR.id("firefly"), FabricParticleTypes.simple(false));

    public static final TagKey<Structure> ON_BASTION_MAPS = TagKey.of(RegistryKeys.STRUCTURE, Beryllium.EXPLORING_CONSTRUCTOR.id("on_bastion_maps"));
    public static final TagKey<Structure> ON_FORTRESS_MAPS = TagKey.of(RegistryKeys.STRUCTURE, Beryllium.EXPLORING_CONSTRUCTOR.id("on_fortress_maps"));
    public static final TagKey<Structure> SPAWN_KEY = TagKey.of(RegistryKeys.STRUCTURE, Beryllium.EXPLORING_CONSTRUCTOR.id("spawn"));

    public static final TagKey<Biome> HAS_FIREFLIES = TagKey.of(RegistryKeys.BIOME, Beryllium.EXPLORING_CONSTRUCTOR.id("has_fireflies"));
    public static final TagKey<Biome> BIRCH_TAG = TagKey.of(RegistryKeys.BIOME, Beryllium.EXPLORING_CONSTRUCTOR.id("birch"));
    public static final TagKey<Biome> SPRUCE_TAG = TagKey.of(RegistryKeys.BIOME, Beryllium.EXPLORING_CONSTRUCTOR.id("spruce"));
    public static final TagKey<Biome> OAK_TAG = TagKey.of(RegistryKeys.BIOME, Beryllium.EXPLORING_CONSTRUCTOR.id("oak"));

    public static final TagKey<Block> VOID_FIRE_BASE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, Beryllium.EXPLORING_CONSTRUCTOR.id("void_fire_base_blocks"));
    public static final TagKey<Block> POINTED_BLOCKS = TagKey.of(RegistryKeys.BLOCK, Beryllium.EXPLORING_CONSTRUCTOR.id("pointed_blocks"));
    public static final TagKey<Block> CHORUS_PLANTABLE = TagKey.of(RegistryKeys.BLOCK, Beryllium.EXPLORING_CONSTRUCTOR.id("chorus_plantable"));

    public static final RiverAlgaeFeature RIVER_ALGAE_FEATURE = Registry.register(Registries.FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("river_algae"), new RiverAlgaeFeature());
    public static final Feature<BasaltColumnsFeatureConfig> BONE_SPIKES = Beryllium.EXPLORING_CONSTRUCTOR.feature("bone_spikes", new BoneSpikesFeature(BasaltColumnsFeatureConfig.CODEC));

    public static final RegistryKey<Biome> CORRUPTED_FOREST = RegistryKey.of(RegistryKeys.BIOME, Beryllium.EXPLORING_CONSTRUCTOR.id("corrupted_forest"));

    public static final RegistryKey<ConfiguredFeature<?, ?>> SWAMP_ALGAE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("swamp_algae"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> RIVER_ALGAE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("river_algae"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DAFFODILS_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("daffodils"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> SCILLA_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("scilla"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> GERANIUMS_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("geraniums"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> MAGMA_DELTA_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("magma_delta"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> BONE_SPIKES_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("bone_spikes"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> CORRUPTED_TREE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("corrupted_tree"));

    public static final RegistryKey<PlacedFeature> SWAMP_ALGAE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("swamp_algae"));
    public static final RegistryKey<PlacedFeature> RIVER_ALGAE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("river_algae"));
    public static final RegistryKey<PlacedFeature> DAFFODILS_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("daffodils"));
    public static final RegistryKey<PlacedFeature> SCILLA_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("scilla"));
    public static final RegistryKey<PlacedFeature> GERANIUMS_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("geraniums"));
    public static final RegistryKey<PlacedFeature> MAGMA_DELTA_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("magma_delta"));
    public static final RegistryKey<PlacedFeature> BONE_SPIKES_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("bone_spikes"));
    public static final RegistryKey<PlacedFeature> CORRUPTED_TREE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Beryllium.EXPLORING_CONSTRUCTOR.id("corrupted_tree"));

    public static final SaplingGenerator CORRUPTED_SAPLING_GENERATOR = new SaplingGenerator(Beryllium.EXPLORING_CONSTRUCTOR.id("corrupted").toString(), Optional.empty(), Optional.of(CORRUPTED_TREE_CONFIGURED), Optional.empty());

    public static final RegistryEntry<StatusEffect> SHATTERED = Beryllium.EXPLORING_CONSTRUCTOR.statusEffect("shattered", new StatusEffect(StatusEffectCategory.HARMFUL, 0x5B345B){});

    public static final Block ONYX_ORE = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("onyx_ore", new OnyxBlock(AbstractBlock.Settings.copy(Blocks.END_STONE).strength(6, 18)), new Item.Settings());
    public static final Block CORRUPTED_END_STONE = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("corrupted_end_stone", new Block(AbstractBlock.Settings.copy(Blocks.END_STONE)), new Item.Settings());
    public static final Block CONGEALED_VOID = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("congealed_void", new CongealedVoidBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.MAGENTA)
            .sounds(BlockSoundGroup.SLIME)
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)), new Item.Settings());
    public static final Block CORRUPTED_LOG = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("corrupted_log", new PillarBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_STEM)), new Item.Settings());
    public static final Block CORRUPTED_WOOD = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("corrupted_wood", new PillarBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_HYPHAE)), new Item.Settings());
    public static final Block STRIPPED_CORRUPTED_LOG = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("stripped_corrupted_log", new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_STEM)), new Item.Settings());
    public static final Block STRIPPED_CORRUPTED_WOOD = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("stripped_corrupted_wood", new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_HYPHAE)), new Item.Settings());
    public static final BlockFamily CORRUPTED_FAMILY = Beryllium.EXPLORING_CONSTRUCTOR.blockFamily("corrupted_plank", Blocks.CRIMSON_PLANKS)
            .base("corrupted_planks")
            .stairs()
            .slab()
            .fence()
            .fenceGate(WoodType.CRIMSON)
            .sign(WoodType.CRIMSON)
            .wallSign(WoodType.CRIMSON)
            .pressurePlate(BlockSetType.CRIMSON)
            .button(BlockSetType.CRIMSON, 30)
            .trapdoor(BlockSetType.CRIMSON)
            .door(BlockSetType.CRIMSON)
            .signItem()
            .build();
    public static final Block CORRUPTED_SAPLING = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("corrupted_sapling", new SaplingBlock(CORRUPTED_SAPLING_GENERATOR, AbstractBlock.Settings.copy(Blocks.CRIMSON_FUNGUS)) {
        @Override
        protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
            return floor.isOf(CORRUPTED_END_STONE) || floor.isOf(Blocks.END_STONE);
        }
    }, new Item.Settings());
    public static final Block FIREFLY_BOTTLE =
            Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem(
                    "firefly_bottle",
                    new Block(AbstractBlock.Settings.create()
                            .strength(0)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .emissiveLighting((state, world, pos) -> true)
                            .luminance(state -> 3)
                            .breakInstantly()
                            .noBlockBreakParticles()
                            .nonOpaque()
                            .sounds(BlockSoundGroup.GLASS)
                            .suffocates(Blocks::never)
                            .blockVision(Blocks::never)),
                    new Item.Settings()
            );
    public static final Block DAFFODILS = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("daffodils", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public static final Block SCILLA = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("scilla", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public static final Block GERANIUMS = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("geraniums", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public static final Block ALGAE_BLOCK = Beryllium.EXPLORING_CONSTRUCTOR.block("algae", new AlgaeBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_GREEN)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .sounds(BlockSoundGroup.GLOW_LICHEN)
            .nonOpaque()
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)));
    public static final Block VOID_FIRE = Beryllium.EXPLORING_CONSTRUCTOR.block("void_fire", new VoidFireBlock(AbstractBlock.Settings.copy(Blocks.SOUL_FIRE).mapColor(MapColor.MAGENTA)));
    public static final Block POINTED_BONE = Beryllium.EXPLORING_CONSTRUCTOR.blockWithItem("pointed_bone", new PointedBoneBlock(AbstractBlock.Settings.copy(Blocks.POINTED_DRIPSTONE)
            .mapColor(MapColor.PALE_YELLOW)
            .requiresTool()
            .strength(2)
            .sounds(BlockSoundGroup.BONE)), new Item.Settings());
    public static final Block BLAZING_TORCH = Beryllium.EXPLORING_CONSTRUCTOR.block("blazing_torch", new BlazingTorchBlock(ParticleTypes.FIREWORK, AbstractBlock.Settings.copy(Blocks.TORCH)));
    public static final Block BLAZING_WALL_TORCH = Beryllium.EXPLORING_CONSTRUCTOR.block("blazing_wall_torch", new BlazingWallTorchBlock(ParticleTypes.FIREWORK, AbstractBlock.Settings.copy(Blocks.TORCH)));

    public static final BlockEntityType<BlazingTorchBlockEntity> BLAZING_TORCH_BLOCK_ENTITY = Beryllium.EXPLORING_CONSTRUCTOR.blockEntity("blazing_torch", BlockEntityType.Builder.create(BlazingTorchBlockEntity::new, BLAZING_TORCH, BLAZING_WALL_TORCH));

    public static final Item ONYX = Beryllium.EXPLORING_CONSTRUCTOR.item("onyx", new Item(new Item.Settings()));
    public static final Item METAL_DETECTOR_ITEM = Beryllium.EXPLORING_CONSTRUCTOR.item("metal_detector", new MetalDetectorItem(new Item.Settings()));
    public static final Item ALGAE_ITEM = Beryllium.EXPLORING_CONSTRUCTOR.item("algae", new AlgaeItem(ALGAE_BLOCK, new Item.Settings()));
    public static final Item EXODINE_INGOT = Beryllium.EXPLORING_CONSTRUCTOR.item("exodine_ingot", new Item(new Item.Settings()));
    public static final Item TEST_STICK = Beryllium.EXPLORING_CONSTRUCTOR.item("test_stick", new Item(new Item.Settings()) {
        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            if (!world.isClient) {
                DragonFireballEntity fireball = new DragonFireballEntity(world, user, user.getRotationVector().multiply(2));

                world.spawnEntity(fireball);
            }

            return super.use(world, user, hand);
        }
    });
    public static final Item BLAZING_TORCH_ITEM = Beryllium.EXPLORING_CONSTRUCTOR.item("blazing_torch", new VerticallyAttachableBlockItem(BLAZING_TORCH, BLAZING_WALL_TORCH, new Item.Settings(), Direction.DOWN) {
        @Override
        public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
            if (slot == PlayerInventory.OFF_HAND_SLOT || selected) {
                BlazingTorchBlock.burn(world, entity.getBlockPos());
            }
        }
    });

    public static final RegistryEntry<EntityAttribute> PLAYER_AIR_MINING_EFFICIENCY = Beryllium.EXPLORING_CONSTRUCTOR.attribute("player.air_mining_efficiency", new ClampedEntityAttribute("attribute.beryllium.exploring.name.player.stable_footing", 0.2, 0.00001, 1).setTracked(true));

    public static final Int2ObjectMap<TradeOffers.Factory[]> ENDERMAN_TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
            1, new TradeOffers.Factory[]{
            },
            2, new TradeOffers.Factory[]{
            }
    ));

    public static final ComponentKey<ReelingComponent> REELING = ComponentRegistryV3.INSTANCE.getOrCreate(Beryllium.EXPLORING_CONSTRUCTOR.id("reeling"), ReelingComponent.class);

    @Override
    public void onTerraBlenderInitialized() {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.END, Beryllium.MOD_ID, createEndRule());
        EndBiomeRegistry.registerHighlandsBiome(CORRUPTED_FOREST, 3);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(TridentEntity.class, REELING, ReelingComponent::new);
    }

    @Override
    public void onInitialize() {
        StrippableBlockRegistry.register(CORRUPTED_LOG, STRIPPED_CORRUPTED_LOG);
        StrippableBlockRegistry.register(CORRUPTED_WOOD, STRIPPED_CORRUPTED_WOOD);
        FlammableBlockRegistry.getDefaultInstance().add(DAFFODILS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(SCILLA, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(GERANIUMS, 60, 100);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    entries.addAfter(Items.COMPASS, METAL_DETECTOR_ITEM);
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
                .register(entries -> {
                    entries.addAfter(Items.PINK_PETALS, DAFFODILS, SCILLA, GERANIUMS);
                });
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(Items.COMPASS, builder -> builder.add(COMPASS_NEEDLE_COMPONENT, DyeColor.RED)));
        Registry.register(Registries.RECIPE_TYPE, Beryllium.EXPLORING_CONSTRUCTOR.id("compass_dye"), new RecipeType<>() {
            @Override
            public String toString() {
                return "exploring/compass_dye";
            }
        });
        Registry.register(Registries.RECIPE_SERIALIZER, Beryllium.EXPLORING_CONSTRUCTOR.id("compass_dye"), CompassDyeRecipe.SERIALIZER);
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SWAMP),
                GenerationStep.Feature.VEGETAL_DECORATION,
                SWAMP_ALGAE_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.RIVER),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RIVER_ALGAE_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BIRCH_TAG),
                GenerationStep.Feature.VEGETAL_DECORATION,
                DAFFODILS_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(SPRUCE_TAG),
                GenerationStep.Feature.VEGETAL_DECORATION,
                SCILLA_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(OAK_TAG),
                GenerationStep.Feature.VEGETAL_DECORATION,
                GERANIUMS_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                BONE_SPIKES_PLACED
        );
        BiomeModifications.create(Beryllium.EXPLORING_CONSTRUCTOR.id("fireflies"))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(HAS_FIREFLIES), context -> {
                    context.getEffects().setParticleConfig(new BiomeParticleConfig(FIREFLY_PARTICLE, 0.008f));
                });
        BiomeModifications.create(Beryllium.EXPLORING_CONSTRUCTOR.id("swamp_water"))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(BiomeKeys.SWAMP), context -> {
                    context.getEffects().setWaterColor(0x6D6D5C);
                    context.getEffects().setWaterFogColor(0x6D6D5C);
                });
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            switch (key.getValue().toString()) {
                case "minecraft:chests/village/village_armorer": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.IRON_CHESTPLATE))
                            .with(ItemEntry.builder(Items.IRON_LEGGINGS))
                            .with(ItemEntry.builder(Items.IRON_BOOTS))
                            .with(ItemEntry.builder(Items.SHIELD))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_butcher", "minecraft:chests/village/village_shepherd",
                     "minecraft:chests/village/village_tannery", "minecraft:chests/village/village_temple": {
                    builder.modifyPools(pool -> pool.bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_cartographer": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(FIREFLY_BOTTLE))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_desert_house": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.APPLE).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3)))));
                    break;
                }
                case "minecraft:chests/village/village_fisher": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.FISHING_ROD).weight(4))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_fletcher": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.IRON_INGOT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))
                            .with(ItemEntry.builder(Items.GRAVEL).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 8))))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_mason": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.BRICK).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))
                            .with(ItemEntry.builder(Items.CRACKED_STONE_BRICKS).weight(2))
                            .with(ItemEntry.builder(Items.CHISELED_STONE_BRICKS).weight(2))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_plains_house": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.WHEAT_SEEDS).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 5))))
                            .with(ItemEntry.builder(GERANIUMS)));
                    break;
                }
                case "minecraft:chests/village/village_savanna_house": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.APPLE).weight(7).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))
                            .with(ItemEntry.builder(Items.WATER_BUCKET)));
                    break;
                }
                case "minecraft:chests/village/village_snowy_house": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.ICE).weight(5)));
                    break;
                }
                case "minecraft:chests/village/village_taiga_house": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(SCILLA)));
                    break;
                }
                case "minecraft:chests/village/village_toolsmith": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.IRON_AXE).weight(5))
                            .with(ItemEntry.builder(Items.IRON_HOE).weight(5))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/village/village_weaponsmith": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.IRON_LEGGINGS).weight(5))
                            .with(ItemEntry.builder(Items.OBSIDIAN).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 7))))
                            .bonusRolls(new ConstantLootNumberProvider(3)));
                    break;
                }
                case "minecraft:chests/jungle_temple": {
                    builder.pool(LootPool.builder()
                            .rolls(new ConstantLootNumberProvider(1))
                            .with(ItemEntry.builder(Items.GOLDEN_HELMET))
                            .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE))
                            .with(ItemEntry.builder(Items.GOLDEN_LEGGINGS))
                            .with(ItemEntry.builder(Items.GOLDEN_BOOTS))
                    );
                    builder.pool(LootPool.builder()
                            .rolls(new ConstantLootNumberProvider(2))
                            .with(ItemEntry.builder(Items.DIAMOND).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))
                            .with(ItemEntry.builder(Items.EMERALD).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))
                            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 4))))
                            .with(ItemEntry.builder(Items.IRON_INGOT).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 5))))
                    );
                    break;
                }
                case "minecraft:gameplay/piglin_bartering": {
                    builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.COMPASS).weight(40)
                                    .apply(new ExplorationCompassLootFunction.Builder()
                                            .withDestination(ON_BASTION_MAPS)
                                            .searchRadius(100)
                                            .withSkipExistingChunks(false)
                                    )
                                    .apply(SetNameLootFunction.builder(Text.translatable("item.beryllium.exploring.bastion_compass"), SetNameLootFunction.Target.ITEM_NAME))
                            )
                            .with(ItemEntry.builder(Items.COMPASS).weight(40)
                                    .apply(new ExplorationCompassLootFunction.Builder()
                                            .withDestination(ON_FORTRESS_MAPS)
                                            .searchRadius(100)
                                            .withSkipExistingChunks(false)
                                    )
                                    .apply(SetNameLootFunction.builder(Text.translatable("item.beryllium.exploring.fortress_compass"), SetNameLootFunction.Target.ITEM_NAME))
                            ));

                    break;
                }
            }
        });
    }

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(METAL_DETECTOR_ITEM, Identifier.ofVanilla("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> MetalDetectorItem.nearestOre(entity, world)));
        ParticleFactoryRegistry.getInstance().register(
                FIREFLY_PARTICLE,
                FireflyFactory::new
        );
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                DyeColor color = stack.get(COMPASS_NEEDLE_COMPONENT);

                if (color == null) {
                    color = DyeColor.RED;
                }

                return color.getSignColor() | 0xFF000000;
            }

            return -1;
        }, Items.COMPASS);
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                float hue = (System.currentTimeMillis() % 10000) / 5000f - 1;
                float sat = (System.currentTimeMillis() % 7000) / 3500f - 1;

                hue = hue * hue;
                sat = sat * sat;

                return Color.HSBtoRGB(Math.abs(hue), sat / 4 + 0.5f, 1);
            }

            return -1;
        }, EXODINE_INGOT);
        ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> {
            if (index != 0) {
                return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
            } else {
                return -1;
            }
        }, DAFFODILS, SCILLA, GERANIUMS);
        ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> {
            return 0xFF0000;/*world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor()*/
        }, ALGAE_BLOCK);
        BlockRenderLayerMap.INSTANCE.putBlock(ALGAE_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(FIREFLY_BOTTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DAFFODILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SCILLA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GERANIUMS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(VOID_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(POINTED_BONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLAZING_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLAZING_WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CONGEALED_VOID, RenderLayer.getTranslucent());
    }

    public static MaterialRules.MaterialRule createEndRule() {
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(CORRUPTED_FOREST),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, MaterialRules.block(CORRUPTED_END_STONE.getDefaultState()))
                ),
                VanillaSurfaceRules.getEndStoneRule()
        );
    }
}
