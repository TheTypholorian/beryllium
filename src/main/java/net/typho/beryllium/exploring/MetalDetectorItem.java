package net.typho.beryllium.exploring;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;

public class MetalDetectorItem extends Item {
    public record OrePeak(int yMin, int yMax, @Nullable Collection<RegistryKey<Biome>> biomes) {
        public OrePeak(int yMin, int yMax) {
            this(yMin, yMax, null);
        }

        public boolean check(World world, BlockPos pos) {
            if (biomes != null) {
                RegistryKey<Biome> current = world.getBiome(pos).getKey().orElse(null);

                if (!biomes.contains(current)) {
                    return false;
                }
            }

            return pos.getY() >= yMin && pos.getY() <= yMax;
        }
    }

    public static final Map<Block, Color> BLOCK_COLORS = new HashMap<>();
    public static final Map<RegistryKey<DimensionType>, Collection<Block>> BLOCK_DIMENSIONS = new HashMap<>();
    public static final List<Block> BLACKLIST = new LinkedList<>(List.of(Blocks.CLAY, Blocks.DIRT, Blocks.GRAVEL, Blocks.INFESTED_STONE));

    public static final Map<Block, List<OrePeak>> ORE_PEAKS = new HashMap<>();

    static {
        BLOCK_COLORS.put(Blocks.ANCIENT_DEBRIS, new Color(101, 71, 64));
        BLOCK_COLORS.put(Blocks.ANDESITE, new Color(138, 138, 142));
        BLOCK_COLORS.put(Blocks.BLACKSTONE, new Color(60, 57, 71));
        BLOCK_COLORS.put(Blocks.CLAY, new Color(161, 167, 177));
        BLOCK_COLORS.put(Blocks.COAL_ORE, new Color(37, 37, 37));
        BLOCK_COLORS.put(Blocks.COPPER_ORE, new Color(224, 115, 77));
        BLOCK_COLORS.put(Blocks.DIAMOND_ORE, new Color(30, 208, 214));
        BLOCK_COLORS.put(Blocks.DIORITE, new Color(190, 191, 193));
        BLOCK_COLORS.put(Blocks.DIRT, new Color(121, 85, 58));
        BLOCK_COLORS.put(Blocks.EMERALD_ORE, new Color(23, 197, 68));
        BLOCK_COLORS.put(Blocks.GOLD_ORE, new Color(252, 238, 75));
        BLOCK_COLORS.put(Blocks.GRANITE, new Color(159, 107, 88));
        BLOCK_COLORS.put(Blocks.GRAVEL, new Color(129, 127, 127));
        BLOCK_COLORS.put(Blocks.INFESTED_STONE, new Color(127, 127, 127));
        BLOCK_COLORS.put(Blocks.IRON_ORE, new Color(216, 175, 147));
        BLOCK_COLORS.put(Blocks.LAPIS_ORE, new Color(68, 111, 220));
        BLOCK_COLORS.put(Blocks.MAGMA_BLOCK, new Color(244, 133, 34));
        BLOCK_COLORS.put(Blocks.NETHER_GOLD_ORE, new Color(252, 238, 75));
        BLOCK_COLORS.put(Blocks.NETHER_QUARTZ_ORE, new Color(212, 202, 186));
        BLOCK_COLORS.put(Blocks.REDSTONE_ORE, new Color(255, 0, 0));
        BLOCK_COLORS.put(Blocks.SOUL_SAND, new Color(73, 55, 44));
        BLOCK_COLORS.put(Blocks.TUFF, new Color(133, 131, 123));
        BLOCK_DIMENSIONS.put(DimensionTypes.OVERWORLD, new LinkedList<>(List.of(
                Blocks.ANDESITE,
                Blocks.CLAY,
                Blocks.COAL_ORE,
                Blocks.COPPER_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.DIORITE,
                Blocks.DIRT,
                Blocks.EMERALD_ORE,
                Blocks.GOLD_ORE,
                Blocks.GRANITE,
                Blocks.GRAVEL,
                Blocks.INFESTED_STONE,
                Blocks.IRON_ORE,
                Blocks.LAPIS_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.TUFF
        )));
        BLOCK_DIMENSIONS.put(DimensionTypes.THE_NETHER, new LinkedList<>(List.of(
                Blocks.ANCIENT_DEBRIS,
                Blocks.BLACKSTONE,
                Blocks.MAGMA_BLOCK,
                Blocks.NETHER_GOLD_ORE,
                Blocks.NETHER_QUARTZ_ORE,
                Blocks.SOUL_SAND
        )));

        ORE_PEAKS.put(Blocks.COAL_ORE, new LinkedList<>(List.of(
                new OrePeak(64, 254)
        )));
        ORE_PEAKS.put(Blocks.COPPER_ORE, new LinkedList<>(List.of(
                new OrePeak(32, 64),
                new OrePeak(16, 80, List.of(BiomeKeys.DRIPSTONE_CAVES))
        )));
        ORE_PEAKS.put(Blocks.DIAMOND_ORE, new LinkedList<>(List.of(
                new OrePeak(-64, -32)
        )));
        ORE_PEAKS.put(Blocks.EMERALD_ORE, new LinkedList<>(List.of(
                new OrePeak(200, 254, List.of(BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.STONY_PEAKS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.SNOWY_SLOPES))
        )));
        ORE_PEAKS.put(Blocks.GOLD_ORE, new LinkedList<>(List.of(
                new OrePeak(-32, 0),
                new OrePeak(32, 254, List.of(BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS, BiomeKeys.WOODED_BADLANDS))
        )));
        ORE_PEAKS.put(Blocks.IRON_ORE, new LinkedList<>(List.of(
                new OrePeak(200, 254),
                new OrePeak(0, 32)
        )));
        ORE_PEAKS.put(Blocks.LAPIS_ORE, new LinkedList<>(List.of(
                new OrePeak(-16, 16)
        )));
        ORE_PEAKS.put(Blocks.REDSTONE_ORE, new LinkedList<>(List.of(
                new OrePeak(-64, -32)
        )));
    }

    public MetalDetectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && player.getInventory().contains(stack)) {
            tooltip.add(Text.translatable("item.beryllium.metal_detector.y_level", (int) player.getY()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));

            boolean[] found = {false};

            ORE_PEAKS.forEach((block, peaks) -> {
                found[0] = true;

                for (OrePeak peak : peaks) {
                    if (peak.check(player.getWorld(), player.getBlockPos())) {
                        tooltip.add(Text.translatable(block.getTranslationKey()).setStyle(Style.EMPTY.withColor(BLOCK_COLORS.computeIfAbsent(block, b -> new Color(b.getDefaultMapColor().color)).getRGB())));
                        break;
                    }
                }
            });

            if (!found[0]) {
                tooltip.add(Text.translatable("item.beryllium.metal_detector.no_ores").setStyle(Style.EMPTY.withBold(true).withColor(Formatting.RED)));
            }
        }

        /*
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && player.getInventory().contains(stack)) {
            ServerWorld world = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getWorld(player.getWorld().getRegistryKey());

            if (world != null) {
                int playerY = (int) player.getY();

                tooltip.add(Text.translatable("item.beryllium.metal_detector.y_level", playerY).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));

                Registry<PlacedFeature> reg = world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE);
                List<Block> blocks = new LinkedList<>();
                Collection<Block> dimension = BLOCK_DIMENSIONS.get(world.getDimensionEntry().getKey().orElse(null));

                for (Identifier id : reg.getIds()) {
                    PlacedFeature feature = reg.get(id);

                    if (feature != null) {
                        ConfiguredFeature<?, ?> config = feature.feature().value();

                        if (config.config() instanceof OreFeatureConfig oreConfig) {
                            Block block = oreConfig.targets.getFirst().state.getBlock();

                            if (block != null && (dimension == null || dimension.contains(block)) && !BLACKLIST.contains(block)) {
                                feature.placementModifiers().stream()
                                        .filter(m -> m instanceof HeightRangePlacementModifier)
                                        .map(m -> (HeightRangePlacementModifier) m)
                                        .findFirst()
                                        .ifPresent(range -> {
                                            HeightRangePlacementModifierAccessor accessor = HeightRangePlacementModifierAccessor.cast(range);
                                            HeightProvider provider = accessor.height();

                                            switch (provider) {
                                                case ConstantHeightProvider constant ->
                                                        tooltip.add(Text.literal("Yet to implement ConstantHeightProvider for " + block));
                                                case UniformHeightProvider uniform -> {
                                                    if (serialize(UniformHeightProvider.UNIFORM_CODEC, uniform) instanceof NbtCompound nbt) {
                                                        int min = getY(nbt.getCompound("min_inclusive"), world),
                                                                max = getY(nbt.getCompound("max_inclusive"), world);

                                                        if (playerY >= min && playerY <= max) {
                                                            blocks.add(block);
                                                        }
                                                    }
                                                }
                                                case BiasedToBottomHeightProvider biased ->
                                                        tooltip.add(Text.literal("Yet to implement BiasedToBottomHeightProvider for " + block));
                                                case VeryBiasedToBottomHeightProvider biased ->
                                                        tooltip.add(Text.literal("Yet to implement VeryBiasedToBottomHeightProvider for " + block));
                                                case TrapezoidHeightProvider trapezoid -> {
                                                    if (serialize(TrapezoidHeightProvider.CODEC, trapezoid) instanceof NbtCompound nbt) {
                                                        int min = getY(nbt.getCompound("min_inclusive"), world),
                                                                max = getY(nbt.getCompound("max_inclusive"), world),
                                                                plateau = nbt.getInt("plateau");
                                                        int half = (max - min - (plateau == 0 ? 20 : plateau)) / 2;

                                                        if (playerY >= min + half && playerY <= max - half) {
                                                            blocks.add(block);
                                                        }
                                                    }
                                                }
                                                case WeightedListHeightProvider weighted ->
                                                        tooltip.add(Text.literal("Yet to implement WeightedListHeightProvider for " + block));
                                                case null -> {
                                                }
                                                default ->
                                                        tooltip.add(Text.literal("\tUh oh! Unrecognized HeightProvider class " + provider.getClass() + " (this is a code thing)").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                                            }
                                        });
                            }
                        }
                    }
                }

                if (blocks.isEmpty()) {
                    tooltip.add(Text.translatable("item.beryllium.metal_detector.no_ores").setStyle(Style.EMPTY.withBold(true).withColor(Formatting.RED)));
                } else {
                    tooltip.add(Text.translatable("item.beryllium.metal_detector.found_ores").setStyle(Style.EMPTY.withBold(true)));

                    for (Block block : blocks) {
                        tooltip.add(Text.translatable(block.getTranslationKey()).setStyle(Style.EMPTY.withColor(BLOCK_COLORS.computeIfAbsent(block, b -> new Color(b.getDefaultMapColor().color)).getRGB())));
                    }
                }
            }
        }
         */
    }

    public static <A> NbtElement serialize(Codec<A> codec, A instance) {
        DataResult<NbtElement> result = codec.encodeStart(NbtOps.INSTANCE, instance);
        return result.getOrThrow();
    }

    public static <A> NbtElement serialize(MapCodec<A> codec, A instance) {
        return serialize(codec.codec(), instance);
    }

    public static int getY(NbtCompound nbt, ServerWorld world) {
        if (nbt.contains("absolute")) {
            return nbt.getInt("absolute");
        } else if (nbt.contains("above_bottom")) {
            return world.getBottomY() + nbt.getInt("above_bottom");
        } else if (nbt.contains("below_top")) {
            return world.getHeight() - nbt.getInt("below_top");
        } else {
            throw new IllegalStateException("Couldn't parse " + nbt);
        }
    }
}
