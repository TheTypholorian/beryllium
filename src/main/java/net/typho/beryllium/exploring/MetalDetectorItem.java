package net.typho.beryllium.exploring;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.typho.beryllium.config.BerylliumConfig;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MetalDetectorItem extends Item {
    public static final Map<Block, Color> BLOCK_COLORS = new HashMap<>();

    static {
        BLOCK_COLORS.put(Blocks.ANCIENT_DEBRIS, new Color(101, 71, 64));
        BLOCK_COLORS.put(Blocks.COAL_ORE, new Color(37, 37, 37));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_COAL_ORE, new Color(37, 37, 37));
        BLOCK_COLORS.put(Blocks.COPPER_ORE, new Color(224, 115, 77));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_COPPER_ORE, new Color(224, 115, 77));
        BLOCK_COLORS.put(Blocks.DIAMOND_ORE, new Color(30, 208, 214));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_DIAMOND_ORE, new Color(30, 208, 214));
        BLOCK_COLORS.put(Blocks.EMERALD_ORE, new Color(23, 197, 68));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_EMERALD_ORE, new Color(23, 197, 68));
        BLOCK_COLORS.put(Blocks.GOLD_ORE, new Color(252, 238, 75));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_GOLD_ORE, new Color(252, 238, 75));
        BLOCK_COLORS.put(Blocks.IRON_ORE, new Color(216, 175, 147));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_IRON_ORE, new Color(216, 175, 147));
        BLOCK_COLORS.put(Blocks.LAPIS_ORE, new Color(68, 111, 220));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_LAPIS_ORE, new Color(68, 111, 220));
        BLOCK_COLORS.put(Blocks.NETHER_GOLD_ORE, new Color(252, 238, 75));
        BLOCK_COLORS.put(Blocks.NETHER_QUARTZ_ORE, new Color(212, 202, 186));
        BLOCK_COLORS.put(Blocks.REDSTONE_ORE, new Color(255, 0, 0));
        BLOCK_COLORS.put(Blocks.DEEPSLATE_REDSTONE_ORE, new Color(255, 0, 0));
        BLOCK_COLORS.put(Blocks.BUDDING_AMETHYST, new Color(166, 120, 241));
    }

    public MetalDetectorItem(Settings settings) {
        super(settings);
    }

    public static GlobalPos nearestOre(Entity entity, World world) {
        BlockPos origin = entity.getBlockPos();
        Set<BlockPos> found = new HashSet<>();

        int xRad = BerylliumConfig.METAL_DETECTOR_RADIUS.get(), yRad = BerylliumConfig.METAL_DETECTOR_HEIGHT.get();

        for (int y = -yRad; y <= yRad; y++) {
            int by = origin.getY() + y;

            for (int x = -xRad; x <= xRad; x++) {
                int bx = origin.getX() + x;

                for (int z = -xRad; z <= xRad; z++) {
                    int bz = origin.getZ() + z;
                    BlockPos pos = new BlockPos(bx, by, bz);

                    if (BLOCK_COLORS.containsKey(world.getBlockState(pos).getBlock())) {
                        found.add(pos);
                    }
                }
            }
        }

        return new GlobalPos(
                world.getRegistryKey(),
                found.stream()
                        .min(Comparator.comparingDouble(pos -> pos.getSquaredDistance(origin)))
                        .orElseGet(() -> {
                            int age = entity.age % 8;
                            return switch (age) {
                                case 0 -> new BlockPos(origin.getX() + 1, origin.getY(), origin.getZ());
                                case 1 -> new BlockPos(origin.getX() + 1, origin.getY(), origin.getZ() + 1);
                                case 2 -> new BlockPos(origin.getX(), origin.getY(), origin.getZ() + 1);
                                case 3 -> new BlockPos(origin.getX() - 1, origin.getY(), origin.getZ() + 1);
                                case 4 -> new BlockPos(origin.getX() - 1, origin.getY(), origin.getZ());
                                case 5 -> new BlockPos(origin.getX() - 1, origin.getY(), origin.getZ() - 1);
                                case 6 -> new BlockPos(origin.getX(), origin.getY(), origin.getZ() - 1);
                                case 7 -> new BlockPos(origin.getX() + 1, origin.getY(), origin.getZ() - 1);
                                default -> origin;
                            };
                        })
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && player.getInventory().contains(stack)) {
            BlockPos playerPos = player.getBlockPos();
            World world = player.getWorld();
            int radius = BerylliumConfig.METAL_DETECTOR_RADIUS.get();
            Map<Block, Integer> found = new HashMap<>();

            if (BerylliumConfig.COMPASS_COORDS.get() && !player.hasReducedDebugInfo()) {
                tooltip.add(Text.translatable("item.beryllium.exploring.compass.pos", playerPos.getX(), playerPos.getY(), playerPos.getZ()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
            }

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block block = world.getBlockState(new BlockPos(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z)).getBlock();

                        if (BLOCK_COLORS.containsKey(block)) {
                            found.compute(block, (k, v) -> v == null ? 1 : v + 1);
                        }
                    }
                }
            }

            found.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(entry -> -entry.getValue()))
                    .forEachOrdered(entry -> tooltip.add(entry.getKey().getName().append(" (" + entry.getValue() + ")").setStyle(Style.EMPTY.withColor(BLOCK_COLORS.get(entry.getKey()).getRGB()))));
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
