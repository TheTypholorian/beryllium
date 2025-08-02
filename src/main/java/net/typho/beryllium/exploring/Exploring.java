package net.typho.beryllium.exploring;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
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
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.BasaltColumnsFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.Constructor;
import net.typho.beryllium.combat.ReelingComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import terrablender.api.EndBiomeRegistry;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

import java.util.Optional;

public class Exploring implements ModInitializer, EntityComponentInitializer, TerraBlenderApi {
    public static final Constructor CONSTRUCTOR = new Constructor("exploring");

    public static final LootFunctionType<ExplorationCompassLootFunction> EXPLORATION_COMPASS = Registry.register(Registries.LOOT_FUNCTION_TYPE, CONSTRUCTOR.id("exploration_compass"), new LootFunctionType<>(ExplorationCompassLootFunction.CODEC));

    public static final ComponentType<DyeColor> COMPASS_NEEDLE_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, CONSTRUCTOR.id("needle_color"), ComponentType.<DyeColor>builder().codec(DyeColor.CODEC).build());

    public static final SimpleParticleType FIREFLY_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, CONSTRUCTOR.id("firefly"), FabricParticleTypes.simple(false));

    public static final TagKey<Structure> ON_BASTION_MAPS = TagKey.of(RegistryKeys.STRUCTURE, CONSTRUCTOR.id("on_bastion_maps"));
    public static final TagKey<Structure> ON_FORTRESS_MAPS = TagKey.of(RegistryKeys.STRUCTURE, CONSTRUCTOR.id("on_fortress_maps"));
    public static final TagKey<Structure> SPAWN_KEY = TagKey.of(RegistryKeys.STRUCTURE, CONSTRUCTOR.id("spawn"));

    public static final TagKey<Biome> HAS_FIREFLIES = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("has_fireflies"));
    public static final TagKey<Biome> BIRCH_TAG = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("birch"));
    public static final TagKey<Biome> SPRUCE_TAG = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("spruce"));
    public static final TagKey<Biome> OAK_TAG = TagKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("oak"));

    public static final TagKey<Block> VOID_FIRE_BASE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, CONSTRUCTOR.id("void_fire_base_blocks"));
    public static final TagKey<Block> POINTED_BLOCKS = TagKey.of(RegistryKeys.BLOCK, CONSTRUCTOR.id("pointed_blocks"));

    public static final RiverAlgaeFeature RIVER_ALGAE_FEATURE = Registry.register(Registries.FEATURE, CONSTRUCTOR.id("river_algae"), new RiverAlgaeFeature());
    public static final Feature<BasaltColumnsFeatureConfig> BONE_SPIKES = CONSTRUCTOR.feature("bone_spikes", new BoneSpikesFeature(BasaltColumnsFeatureConfig.CODEC));

    public static final RegistryKey<Biome> CORRUPTED_FOREST = RegistryKey.of(RegistryKeys.BIOME, CONSTRUCTOR.id("corrupted_forest"));

    public static final RegistryKey<ConfiguredFeature<?, ?>> SWAMP_ALGAE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("swamp_algae"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> RIVER_ALGAE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("river_algae"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DAFFODILS_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("daffodils"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> SCILLA_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("scilla"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> GERANIUMS_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("geraniums"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> MAGMA_DELTA_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("magma_delta"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> BONE_SPIKES_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("bone_spikes"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> CORRUPTED_TREE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CONSTRUCTOR.id("corrupted_tree"));

    public static final RegistryKey<PlacedFeature> SWAMP_ALGAE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("swamp_algae"));
    public static final RegistryKey<PlacedFeature> RIVER_ALGAE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("river_algae"));
    public static final RegistryKey<PlacedFeature> DAFFODILS_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("daffodils"));
    public static final RegistryKey<PlacedFeature> SCILLA_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("scilla"));
    public static final RegistryKey<PlacedFeature> GERANIUMS_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("geraniums"));
    public static final RegistryKey<PlacedFeature> MAGMA_DELTA_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("magma_delta"));
    public static final RegistryKey<PlacedFeature> BONE_SPIKES_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("bone_spikes"));
    public static final RegistryKey<PlacedFeature> CORRUPTED_TREE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, CONSTRUCTOR.id("corrupted_tree"));

    public static final SaplingGenerator CORRUPTED_SAPLING_GENERATOR = new SaplingGenerator(CONSTRUCTOR.id("corrupted").toString(), Optional.empty(), Optional.of(CORRUPTED_TREE_CONFIGURED), Optional.empty());

    public static final Block CORRUPTED_END_STONE = CONSTRUCTOR.blockWithItem("corrupted_end_stone", new Block(AbstractBlock.Settings.copy(Blocks.END_STONE)), new Item.Settings());
    public static final Block CONGEALED_VOID = CONSTRUCTOR.blockWithItem("congealed_void", new CongealedVoidBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.MAGENTA)
            .sounds(BlockSoundGroup.SLIME)
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)), new Item.Settings());
    public static final Block CORRUPTED_LOG = CONSTRUCTOR.blockWithItem("corrupted_log", new PillarBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_STEM)), new Item.Settings());
    public static final Block CORRUPTED_WOOD = CONSTRUCTOR.blockWithItem("corrupted_wood", new PillarBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_HYPHAE)), new Item.Settings());
    public static final Block STRIPPED_CORRUPTED_LOG = CONSTRUCTOR.blockWithItem("stripped_corrupted_log", new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_STEM)), new Item.Settings());
    public static final Block STRIPPED_CORRUPTED_WOOD = CONSTRUCTOR.blockWithItem("stripped_corrupted_wood", new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_HYPHAE)), new Item.Settings());
    public static final Block CORRUPTED_PLANKS = CONSTRUCTOR.blockWithItem("corrupted_planks", new Block(AbstractBlock.Settings.copy(Blocks.CRIMSON_PLANKS)), new Item.Settings());
    public static final Block CORRUPTED_SAPLING = CONSTRUCTOR.blockWithItem("corrupted_sapling", new SaplingBlock(CORRUPTED_SAPLING_GENERATOR, AbstractBlock.Settings.copy(Blocks.CRIMSON_FUNGUS)) {
        @Override
        protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
            return floor.isOf(CORRUPTED_END_STONE) || floor.isOf(Blocks.END_STONE);
        }
    }, new Item.Settings());
    public static final Block FIREFLY_BOTTLE =
            CONSTRUCTOR.blockWithItem(
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
    public static final Block DAFFODILS = CONSTRUCTOR.blockWithItem("daffodils", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public static final Block SCILLA = CONSTRUCTOR.blockWithItem("scilla", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public static final Block GERANIUMS = CONSTRUCTOR.blockWithItem("geraniums", new FlowerbedBlock(AbstractBlock.Settings.copy(Blocks.PINK_PETALS)), new Item.Settings());
    public static final Block ALGAE_BLOCK = CONSTRUCTOR.block("algae", new AlgaeBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_GREEN)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .sounds(BlockSoundGroup.GLOW_LICHEN)
            .nonOpaque()
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)));
    public static final Block VOID_FIRE = CONSTRUCTOR.block("void_fire", new VoidFireBlock(AbstractBlock.Settings.copy(Blocks.SOUL_FIRE).mapColor(MapColor.MAGENTA)));
    public static final Block POINTED_BONE = CONSTRUCTOR.blockWithItem("pointed_bone", new PointedBoneBlock(AbstractBlock.Settings.copy(Blocks.POINTED_DRIPSTONE)
            .mapColor(MapColor.PALE_YELLOW)
            .requiresTool()
            .strength(2)
            .sounds(BlockSoundGroup.BONE)), new Item.Settings());

    public static final Item METAL_DETECTOR_ITEM = CONSTRUCTOR.item("metal_detector", new MetalDetectorItem(new Item.Settings()));
    public static final Item ALGAE_ITEM = CONSTRUCTOR.item("algae", new AlgaeItem(ALGAE_BLOCK, new Item.Settings()));
    public static final Item EXODINE_INGOT = CONSTRUCTOR.item("exodine_ingot", new Item(new Item.Settings()));
    public static final Item TEST_STICK = CONSTRUCTOR. item("test_stick", new Item(new Item.Settings()) {
        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            if (!world.isClient) {
                DragonFireballEntity fireball = new DragonFireballEntity(world, user, user.getRotationVector().multiply(2));

                world.spawnEntity(fireball);
            }

            return super.use(world, user, hand);
        }
    });

    public static final RegistryEntry<EntityAttribute> STABLE_FOOTING = CONSTRUCTOR.attribute("generic.stable_footing", new ClampedEntityAttribute("attribute.beryllium.exploring.name.generic.stable_footing", 0.2, 0.001, 1).setTracked(true));

    public static final Int2ObjectMap<TradeOffers.Factory[]> ENDERMAN_TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
            1, new TradeOffers.Factory[]{
            },
            2, new TradeOffers.Factory[]{
            }
    ));

    public static final ComponentKey<ReelingComponent> REELING = ComponentRegistryV3.INSTANCE.getOrCreate(CONSTRUCTOR.id("reeling"), ReelingComponent.class);

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
        Registry.register(Registries.RECIPE_TYPE, CONSTRUCTOR.id("compass_dye"), new RecipeType<>() {
            @Override
            public String toString() {
                return "exploring/compass_dye";
            }
        });
        Registry.register(Registries.RECIPE_SERIALIZER, CONSTRUCTOR.id("compass_dye"), CompassDyeRecipe.SERIALIZER);
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
        BiomeModifications.create(CONSTRUCTOR.id("fireflies"))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(HAS_FIREFLIES), context -> {
                    context.getEffects().setParticleConfig(new BiomeParticleConfig(FIREFLY_PARTICLE, 0.008f));
                });
        BiomeModifications.create(CONSTRUCTOR.id("swamp_water"))
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

    public static MaterialRules.MaterialRule createEndRule() {
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(CORRUPTED_FOREST),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, MaterialRules.block(CORRUPTED_END_STONE.getDefaultState()))
                ),
                VanillaSurfaceRules.getEndStoneRule()
        );
    }

    public static class Config extends ConfigSection {
        public MetalDetector metalDetector = new MetalDetector();

        public static class MetalDetector extends ConfigSection {
            public int tooltipRadius = 16, needleX = 16, needleY = 2;
        }

        public boolean spawnInVillage = true;
    }
}
