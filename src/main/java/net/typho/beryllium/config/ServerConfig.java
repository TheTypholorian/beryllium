package net.typho.beryllium.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
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
    public static final Map<Identifier, Property<?>> properties = new LinkedHashMap<>();

    public static final Set<Identifier> DURABILITY_ENCHANTS = new HashSet<>(Set.of(
            Identifier.ofVanilla("mending"),
            Identifier.ofVanilla("unbreaking")
    ));

    public static final BooleanProperty fertilizableSugarcane = new BooleanProperty(Beryllium.COMBAT_CONSTRUCTOR.id("fertilizable_sugarcane"), true);
    public static final IntProperty maxSugarcaneHeight = new IntProperty(Beryllium.COMBAT_CONSTRUCTOR.id("max_sugarcane_height"), 5);
    public static final IntProperty enderPearlCooldown = new IntProperty(Beryllium.COMBAT_CONSTRUCTOR.id("ender_pearl_cooldown"), 300);
    public static final FloatProperty enderPearlSpeed = new FloatProperty(Beryllium.COMBAT_CONSTRUCTOR.id("ender_pearl_speed"), 1f);
    public static final IntProperty endCrystalCooldown = new IntProperty(Beryllium.COMBAT_CONSTRUCTOR.id("end_crystal_cooldown"), 30);
    public static final FloatProperty endCrystalPower = new FloatProperty(Beryllium.COMBAT_CONSTRUCTOR.id("end_crystal_power"), 4f);
    public static final BooleanProperty maceRebalance = new BooleanProperty(Beryllium.COMBAT_CONSTRUCTOR.id("mace_rebalance"), true);
    public static final BooleanProperty sweepingMargin = new BooleanProperty(Beryllium.COMBAT_CONSTRUCTOR.id("sweeping_margin"), true);
    public static final FloatProperty sweepMarginMultiplier = new FloatProperty(Beryllium.COMBAT_CONSTRUCTOR.id("sweep_margin_multiplier"), 0.05f);
    public static final BooleanProperty crossbowEndCrystals = new BooleanProperty(Beryllium.COMBAT_CONSTRUCTOR.id("crossbow_end_crystals"), true);
    public static final BooleanProperty respawnAnchorsDontExplode = new BooleanProperty(Beryllium.COMBAT_CONSTRUCTOR.id("respawn_anchors_dont_explode"), true);
    public static final IntProperty shieldMaxDurability = new IntProperty(Beryllium.COMBAT_CONSTRUCTOR.id("shield_max_durability"), 30);
    public static final IntProperty shieldLowerCooldown = new IntProperty(Beryllium.COMBAT_CONSTRUCTOR.id("shield_lower_cooldown"), 60);
    public static final IntProperty splashPotionCooldown = new IntProperty(Beryllium.COMBAT_CONSTRUCTOR.id("splash_potion_cooldown"), 100);

    public static final BooleanProperty disabledArmor = new BooleanProperty(Beryllium.ARMOR_CONSTRUCTOR.id("disabled_armor"), true);
    public static final BooleanProperty betterArmor = new BooleanProperty(Beryllium.ARMOR_CONSTRUCTOR.id("better_armor"), true);

    public static final FeatureProperty extraStoneBricks = new FeatureProperty(Beryllium.BUILDING_CONSTRUCTOR.id("extra_stone_bricks"), true);

    public static final IntProperty metalDetectorRadius = new IntProperty(Beryllium.EXPLORING_CONSTRUCTOR.id("metal_detector_radius"), 16);
    public static final IntProperty metalDetectorHeight = new IntProperty(Beryllium.EXPLORING_CONSTRUCTOR.id("metal_detector_height"), 2);
    public static final BooleanProperty spawnInVillage = new BooleanProperty(Beryllium.EXPLORING_CONSTRUCTOR.id("spawn_in_village"), true);

    public static final BooleanProperty enchantmentCatalysts = new BooleanProperty(Beryllium.ENCHANTING_CONSTRUCTOR.id("enchantment_catalysts"), true);
    public static final BooleanProperty enchantmentCapacity = new BooleanProperty(Beryllium.ENCHANTING_CONSTRUCTOR.id("enchantment_capacity"), true);

    public static final BooleanProperty cropComparatorOutput = new BooleanProperty(Beryllium.REDSTONE_CONSTRUCTOR.id("crop_comparator_output"), true);
    public static final BooleanProperty dispensersPlaceBlocks = new BooleanProperty(Beryllium.REDSTONE_CONSTRUCTOR.id("dispensers_place_blocks"), true);
    public static final IntProperty hopperCooldown = new IntProperty(Beryllium.REDSTONE_CONSTRUCTOR.id("hopper_cooldown"), 4);
    public static final BooleanProperty instantChainTNT = new BooleanProperty(Beryllium.REDSTONE_CONSTRUCTOR.id("instant_chain_tnt"), false);

    public static final BooleanServerResourceProperty durabilityRemoval = new BooleanServerResourceProperty(Beryllium.BASE_CONSTRUCTOR.id("durability_removal"), true);
    public static final BooleanProperty ultraDark = new BooleanProperty(Beryllium.BASE_CONSTRUCTOR.id("ultra_dark"), false);
    public static final BooleanProperty disabledChat = new BooleanProperty(Beryllium.BASE_CONSTRUCTOR.id("disabled_chat"), false);

    public static float ultraDarkBlend(World world) {
        if (!ultraDark.get()) {
            return 0;
        }

        float f = (world.getTimeOfDay() / 12000f + 0.75f) * (float) Math.PI;
        f = (float) (Math.cos(f) * 1.5f);
        f = MathHelper.clamp(f, 0, 1);
        return f;
    }

    public static void read(Dynamic<?> dynamic) {
        for (Property<?> property : properties.values()) {
            property.read(dynamic.get(property.id.toString()));
        }
    }

    public static NbtElement write(DynamicRegistryManager registries) {
        NbtCompound nbt = new NbtCompound();

        for (Property<?> property : properties.values()) {
            nbt.put(property.id.toString(), property.write(registries));
        }

        return nbt;
    }

    public static void encode(ByteBuf buf) {
        for (Property<?> property : properties.values()) {
            property.encode(buf);
        }
    }

    public static Text print() {
        MutableText text = Text.translatable("config.beryllium.title");

        for (Property<?> property : properties.values()) {
            text.append(Text.literal("\n").append(property.display()).append(" = ").append(property.value.toString()));
        }

        return text;
    }

    public static void save(MinecraftServer server) {
        server.session.backupLevelDataFile(server.getRegistryManager(), server.getSaveProperties(), server.getPlayerManager().getUserData());
    }

    public static FeatureSet getEnabledFeatures(FeatureSet set) {
        for (Property<?> property : properties.values()) {
            set = property.getFeatures(set);
        }

        return set;
    }

    public static LiteralArgumentBuilder<ServerCommandSource> command(LiteralArgumentBuilder<ServerCommandSource> command) {
        command.then(LiteralArgumentBuilder.<ServerCommandSource>literal("config")
                .executes(context -> {
                    context.getSource().sendFeedback(ServerConfig::print, false);
                    return 1;
                }));

        for (Property<?> property : properties.values()) {
            command.then(
                    LiteralArgumentBuilder.<ServerCommandSource>literal(property.commandPath())
                            .requires(context -> context.hasPermissionLevel(2))
                            .executes(context -> {
                                context.getSource().sendFeedback(() -> property.display().copy().append(" is set to ").append(property.get().toString()), false);
                                return 1;
                            })
                            .then(
                                    CommandManager.argument("value", property.argumentType)
                                            .executes(context -> {
                                                context.getSource().getServer().execute(() -> {
                                                    Object value = context.getArgument("value", Object.class);
                                                    property.set(value);
                                                    property.updatedServer(context.getSource().getServer());
                                                    save(context.getSource().getServer());

                                                    for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
                                                        ServerPlayNetworking.send(player, new SyncServerConfigS2C(property));
                                                    }

                                                    context.getSource().sendFeedback(() -> property.display().copy().append(" now set to ").append(property.get().toString()), true);
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
