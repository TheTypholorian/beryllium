package net.typho.beryllium.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.redstone.Redstone;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerConfig {
    public static final PacketCodec<ByteBuf, ServerConfig> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public ServerConfig decode(ByteBuf buf) {
            return new ServerConfig(buf);
        }

        @Override
        public void encode(ByteBuf buf, ServerConfig config) {
            config.encode(buf);
        }
    };
    public final Map<Identifier, Property<?>> properties = new LinkedHashMap<>();

    public final BooleanProperty fertilizableSugarcane = new BooleanProperty(this, Combat.CONSTRUCTOR.id("fertilizable_sugarcane"), true);
    public final IntProperty maxSugarcaneHeight = new IntProperty(this, Combat.CONSTRUCTOR.id("max_sugarcane_height"), 5);
    public final IntProperty enderPearlCooldown = new IntProperty(this, Combat.CONSTRUCTOR.id("ender_pearl_cooldown"), 300);
    public final FloatProperty enderPearlSpeed = new FloatProperty(this, Combat.CONSTRUCTOR.id("ender_pearl_speed"), 1f);
    public final IntProperty endCrystalCooldown = new IntProperty(this, Combat.CONSTRUCTOR.id("end_crystal_cooldown"), 30);
    public final FloatProperty endCrystalPower = new FloatProperty(this, Combat.CONSTRUCTOR.id("end_crystal_power"), 4f);
    public final BooleanProperty maceRebalance = new BooleanProperty(this, Combat.CONSTRUCTOR.id("mace_rebalance"), true);
    public final BooleanProperty sweepingMargin = new BooleanProperty(this, Combat.CONSTRUCTOR.id("sweeping_margin"), true);
    public final FloatProperty sweepMarginMultiplier = new FloatProperty(this, Combat.CONSTRUCTOR.id("sweep_margin_multiplier"), 0.05f);
    public final BooleanProperty crossbowEndCrystals = new BooleanProperty(this, Combat.CONSTRUCTOR.id("crossbow_end_crystals"), true);
    public final BooleanProperty respawnAnchorsDontExplode = new BooleanProperty(this, Combat.CONSTRUCTOR.id("respawn_anchors_dont_explode"), true);
    public final BooleanProperty shieldDurability = new BooleanProperty(this, Combat.CONSTRUCTOR.id("shield_durability"), true);
    public final IntProperty shieldMaxDurability = new IntProperty(this, Combat.CONSTRUCTOR.id("shield_max_durability"), 30);
    public final IntProperty shieldLowerCooldown = new IntProperty(this, Combat.CONSTRUCTOR.id("shield_lower_cooldown"), 60);
    public final IntProperty splashPotionCooldown = new IntProperty(this, Combat.CONSTRUCTOR.id("splash_potion_cooldown"), 100);

    public final IntProperty metalDetectorRadius = new IntProperty(this, Exploring.CONSTRUCTOR.id("metal_detector_radius"), 16);
    public final IntProperty metalDetectorHeight = new IntProperty(this, Exploring.CONSTRUCTOR.id("metal_detector_height"), 2);
    public final BooleanProperty spawnInVillage = new BooleanProperty(this, Exploring.CONSTRUCTOR.id("spawn_in_village"), true);

    public final BooleanProperty enchantmentCatalysts = new BooleanProperty(this, Enchanting.CONSTRUCTOR.id("enchantment_catalysts"), true);
    public final BooleanProperty enchantmentCapacity = new BooleanProperty(this, Enchanting.CONSTRUCTOR.id("enchantment_capacity"), true);

    public final BooleanProperty cropComparatorOutput = new BooleanProperty(this, Redstone.CONSTRUCTOR.id("crop_comparator_output"), true);
    public final BooleanProperty dispensersPlaceBlocks = new BooleanProperty(this, Redstone.CONSTRUCTOR.id("dispensers_place_blocks"), true);
    public final IntProperty hopperCooldown = new IntProperty(this, Redstone.CONSTRUCTOR.id("hopper_cooldown"), 2);
    public final BooleanProperty instantChainTNT = new BooleanProperty(this, Redstone.CONSTRUCTOR.id("instant_chain_tnt"), true);

    public final BooleanProperty durabilityRemoval = new BooleanProperty(this, Beryllium.CONSTRUCTOR.id("durability_removal"), true);
    public final BooleanProperty ultraDark = new BooleanProperty(this, Beryllium.CONSTRUCTOR.id("ultra_dark"), true);

    public ServerConfig() {
    }

    public ServerConfig(ByteBuf buf) {
        for (Property<?> property : properties.values()) {
            property.decode(buf);
        }
    }

    public void read(Dynamic<?> dynamic) {
        for (Property<?> property : properties.values()) {
            property.read(dynamic.get(property.id.toString()));
        }
    }

    public NbtElement write(DynamicRegistryManager registries) {
        NbtCompound nbt = new NbtCompound();

        for (Property<?> property : properties.values()) {
            nbt.put(property.id.toString(), property.write(registries));
        }

        return nbt;
    }

    public void encode(ByteBuf buf) {
        for (Property<?> property : properties.values()) {
            property.encode(buf);
        }
    }

    public Text print() {
        MutableText text = Text.translatable("config.beryllium.title");

        for (Property<?> property : properties.values()) {
            text.append(Text.literal("\n").append(property.display()).append(" = ").append(property.value.toString()));
        }

        return text;
    }

    public void save(MinecraftServer server) {
        server.session.backupLevelDataFile(server.getRegistryManager(), server.getSaveProperties(), server.getPlayerManager().getUserData());

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new SyncServerConfigS2C(this));
        }
    }

    public LiteralArgumentBuilder<ServerCommandSource> command(LiteralArgumentBuilder<ServerCommandSource> command) {
        command.then(LiteralArgumentBuilder.<ServerCommandSource>literal("config")
                .executes(context -> {
                    context.getSource().sendFeedback(this::print, false);
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
                                                    save(context.getSource().getServer());
                                                    context.getSource().sendFeedback(() -> property.display().copy().append(" now set to ").append(property.get().toString()), true);
                                                });
                                                return 1;
                                            })
                            )
            );
        }

        return command;
    }
}
