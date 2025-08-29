package net.typho.beryllium.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ServerConfig implements ModInitializer {
    public static final Map<Identifier, Feature<?>> ALL_FEATURES = new LinkedHashMap<>();

    public static final Set<Identifier> DURABILITY_ENCHANTS = new HashSet<>(Set.of(
            Identifier.ofVanilla("mending"),
            Identifier.ofVanilla("unbreaking")
    ));

    public static final FeatureGroup ARMOR_GROUP = new FeatureGroup(Beryllium.ARMOR_CONSTRUCTOR, "config");
    public static final FeatureGroup BUILDING_GROUP = new FeatureGroup(Beryllium.BUILDING_CONSTRUCTOR, "config");
    public static final FeatureGroup CHALLENGES_GROUP = new FeatureGroup(Beryllium.CHALLENGES_CONSTRUCTOR, "config");
    public static final FeatureGroup COMBAT_GROUP = new FeatureGroup(Beryllium.COMBAT_CONSTRUCTOR, "config");
    public static final FeatureGroup ENCHANTING_GROUP = new FeatureGroup(Beryllium.ENCHANTING_CONSTRUCTOR, "config");
    public static final FeatureGroup EXPLORING_GROUP = new FeatureGroup(Beryllium.EXPLORING_CONSTRUCTOR, "config");
    public static final FeatureGroup REDSTONE_GROUP = new FeatureGroup(Beryllium.REDSTONE_CONSTRUCTOR, "config");

    public static final BooleanFeature DURABILITY_REMOVAL = new BooleanFeature(COMBAT_GROUP, "durability_removal", true);
    public static final BooleanFeature FERTILIZABLE_SUGARCANE = new BooleanFeature(COMBAT_GROUP, "fertilizable_sugarcane", true);
    public static final IntFeature MAX_SUGARCANE_HEIGHT = new IntFeature(COMBAT_GROUP, "max_sugarcane_height", 5);
    public static final IntFeature ENDER_PEARL_COOLDOWN = new IntFeature(COMBAT_GROUP, "ender_pearl_cooldown", 300);
    public static final FloatFeature ENDER_PEARL_SPEED = new FloatFeature(COMBAT_GROUP, "ender_pearl_speed", 1f);
    public static final IntFeature END_CRYSTAL_COOLDOWN = new IntFeature(COMBAT_GROUP, "end_crystal_cooldown", 30);
    public static final FloatFeature END_CRYSTAL_POWER = new FloatFeature(COMBAT_GROUP, "end_crystal_power", 4f);
    public static final BooleanFeature MACE_REBALANCE = new BooleanFeature(COMBAT_GROUP, "mace_rebalance", true);
    public static final BooleanFeature SWEEPING_MARGIN = new BooleanFeature(COMBAT_GROUP, "sweeping_margin", true);
    public static final FloatFeature SWEEP_MARGIN_MULTIPLIER = new FloatFeature(COMBAT_GROUP, "sweep_margin_multiplier", 0.05f);
    public static final BooleanFeature CROSSBOW_END_CRYSTALS = new BooleanFeature(COMBAT_GROUP, "crossbow_end_crystals", true);
    public static final BooleanFeature RESPAWN_ANCHORS_DONT_EXPLODE = new BooleanFeature(COMBAT_GROUP, "respawn_anchors_dont_explode", true);
    public static final IntFeature SHIELD_MAX_DURABILITY = new IntFeature(COMBAT_GROUP, "shield_max_durability", 30);
    public static final IntFeature SHIELD_LOWER_COOLDOWN = new IntFeature(COMBAT_GROUP, "shield_lower_cooldown", 60);
    public static final IntFeature SPLASH_POTION_COOLDOWN = new IntFeature(COMBAT_GROUP, "splash_potion_cooldown", 100);

    public static final BooleanFeature DISABLED_ARMOR = new BooleanFeature(ARMOR_GROUP, "disabled_armor", true);
    public static final BooleanFeature BETTER_ARMOR = new BooleanFeature(ARMOR_GROUP, "better_armor", true);

    public static final BooleanFeature ULTRA_DARK = new BooleanFeature(CHALLENGES_GROUP, "ultra_dark", false);
    public static final BooleanFeature DISABLED_CHAT = new BooleanFeature(CHALLENGES_GROUP, "disabled_chat", false);

    public static final BooleanFeature EXTRA_STONE_BRICKS = new BooleanFeature(BUILDING_GROUP, "extra_stone_bricks", true);

    public static final IntFeature METAL_DETECTOR_RADIUS = new IntFeature(EXPLORING_GROUP, "metal_detector_radius", 16);
    public static final IntFeature METAL_DETECTOR_HEIGHT = new IntFeature(EXPLORING_GROUP, "metal_detector_height", 2);
    public static final BooleanFeature SPAWN_IN_VILLAGE = new BooleanFeature(EXPLORING_GROUP, "spawn_in_village", true);

    public static final BooleanFeature ENCHANTMENT_CATALYSTS = new BooleanFeature(ENCHANTING_GROUP, "catalysts", true);
    public static final BooleanFeature ENCHANTMENT_CAPACITY = new BooleanFeature(ENCHANTING_GROUP, "capacity", true);

    public static final BooleanFeature CROP_COMPARATOR_OUTPUT = new BooleanFeature(REDSTONE_GROUP, "crop_comparator_output", true);
    public static final BooleanFeature DISPENSERS_PLACE_BLOCKS = new BooleanFeature(REDSTONE_GROUP, "dispensers_place_blocks", true);
    public static final IntFeature HOPPER_COOLDOWN = new IntFeature(REDSTONE_GROUP, "hopper_cooldown", 4);
    public static final BooleanFeature INSTANT_CHAIN_TNT = new BooleanFeature(REDSTONE_GROUP, "instant_chain_tnt", false);

    public static float ultraDarkBlend(World world) {
        if (!ULTRA_DARK.get()) {
            return 0;
        }

        float f = (world.getTimeOfDay() / 12000f + 0.75f) * (float) Math.PI;
        f = (float) (Math.cos(f) * 1.5f);
        f = MathHelper.clamp(f, 0, 1);
        return f;
    }

    public static void read(Dynamic<?> dynamic) {
        for (Feature<?> feature : ALL_FEATURES.values()) {
            feature.read(dynamic.get(feature.id.toString()));
        }
    }

    public static NbtElement write(DynamicRegistryManager registries) {
        NbtCompound nbt = new NbtCompound();

        for (Feature<?> feature : ALL_FEATURES.values()) {
            nbt.put(feature.id.toString(), feature.write(registries));
        }

        return nbt;
    }

    public static void encode(ByteBuf buf) {
        for (Feature<?> feature : ALL_FEATURES.values()) {
            feature.encode(buf);
        }
    }

    public static Text print() {
        MutableText text = Text.translatable("config.beryllium.title");

        for (Feature<?> feature : ALL_FEATURES.values()) {
            text.append(Text.literal("\n").append(feature.display()).append(" = ").append(feature.value.toString()));
        }

        return text;
    }

    public static void save(MinecraftServer server) {
        server.session.backupLevelDataFile(server.getRegistryManager(), server.getSaveProperties(), server.getPlayerManager().getUserData());
    }

    public static LiteralArgumentBuilder<ServerCommandSource> command(LiteralArgumentBuilder<ServerCommandSource> command) {
        command.then(LiteralArgumentBuilder.<ServerCommandSource>literal("config")
                .executes(context -> {
                    context.getSource().sendFeedback(ServerConfig::print, false);
                    return 1;
                }));

        for (Feature<?> feature : ALL_FEATURES.values()) {
            command.then(
                    LiteralArgumentBuilder.<ServerCommandSource>literal(feature.commandPath())
                            .requires(context -> context.hasPermissionLevel(2))
                            .executes(context -> {
                                context.getSource().sendFeedback(() -> feature.display().copy().append(" is set to ").append(feature.get().toString()), false);
                                return 1;
                            })
                            .then(
                                    CommandManager.argument("value", feature.argumentType)
                                            .executes(context -> {
                                                context.getSource().getServer().execute(() -> {
                                                    Object value = context.getArgument("value", Object.class);
                                                    feature.set(value);
                                                    feature.updatedServer(context.getSource().getServer());
                                                    save(context.getSource().getServer());

                                                    for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
                                                        ServerPlayNetworking.send(player, new SyncServerConfigS2C(feature));
                                                    }

                                                    context.getSource().sendFeedback(() -> feature.display().copy().append(" now set to ").append(feature.get().toString()), true);
                                                });
                                                return 1;
                                            })
                            )
            );
        }

        return command;
    }

    @Override
    public void onInitialize() {
    }
}
