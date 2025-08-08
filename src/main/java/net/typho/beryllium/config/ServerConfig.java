package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.ArgumentType;
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
import net.typho.beryllium.combat.Combat;

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
    public final Map<String, Property<?>> properties = new LinkedHashMap<>();

    public final BooleanProperty fertilizableSugarcane = new BooleanProperty(this, Combat.CONSTRUCTOR, "fertilizableSugarcane", true);
    public final IntProperty maxSugarcaneHeight = new IntProperty(this, Combat.CONSTRUCTOR, "maxSugarcaneHeight", 5);

    public final IntProperty enderPearlCooldown = new IntProperty(this, Combat.CONSTRUCTOR, "enderPearlCooldown", 300);
    public final FloatProperty enderPearlSpeed = new FloatProperty(this, Combat.CONSTRUCTOR, "enderPearlSpeed", 1f);
    public final IntProperty endCrystalCooldown = new IntProperty(this, Combat.CONSTRUCTOR, "endCrystalCooldown", 30);
    public final FloatProperty endCrystalPower = new FloatProperty(this, Combat.CONSTRUCTOR, "endCrystalPower", 4f);
    public final BooleanProperty maceRebalance = new BooleanProperty(this, Combat.CONSTRUCTOR, "maceRebalance", true);
    public final BooleanProperty sweepingMargin = new BooleanProperty(this, Combat.CONSTRUCTOR, "sweepingMargin", true);
    public final FloatProperty sweepMarginMultiplier = new FloatProperty(this, Combat.CONSTRUCTOR, "sweepMarginMultiplier", 0.05f);
    public final BooleanProperty crossbowEndCrystals = new BooleanProperty(this, Combat.CONSTRUCTOR, "crossbowEndCrystals", true);
    public final BooleanProperty respawnAnchorsDontExplode = new BooleanProperty(this, Combat.CONSTRUCTOR, "respawnAnchorsDontExplode", true);
    public final BooleanProperty shieldDurability = new BooleanProperty(this, Combat.CONSTRUCTOR, "shieldDurability", true);
    public final IntProperty shieldMaxDurability = new IntProperty(this, Combat.CONSTRUCTOR, "shieldMaxDurability", 30);
    public final IntProperty shieldLowerCooldown = new IntProperty(this, Combat.CONSTRUCTOR, "shieldLowerCooldown", 60);
    public final IntProperty splashPotionCooldown = new IntProperty(this, Combat.CONSTRUCTOR, "splashPotionCooldown", 100);

    public final BooleanProperty durabilityRemoval = new BooleanProperty(this, "durabilityRemoval", true);

    public final BooleanProperty ultraDark = new BooleanProperty(this, "ultraDark", true);

    public ServerConfig() {
    }

    public ServerConfig(ByteBuf buf) {
        for (Property<?> property : properties.values()) {
            property.decode(buf);
        }
    }

    public void read(Dynamic<?> dynamic) {
        for (Property<?> property : properties.values()) {
            property.read(dynamic.get(property.name));
        }
    }

    public NbtElement write(DynamicRegistryManager registries) {
        NbtCompound nbt = new NbtCompound();

        for (Property<?> property : properties.values()) {
            nbt.put(property.name, property.write(registries));
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

    public ArgumentType<String> keyArgumentType() {
        return new KeyArgumentType(this);
    }

    public LiteralArgumentBuilder<ServerCommandSource> setCommandBuilder() {
        LiteralArgumentBuilder<ServerCommandSource> builder = LiteralArgumentBuilder.literal("set");

        for (Property<?> property : properties.values()) {
            builder.then(
                    LiteralArgumentBuilder.<ServerCommandSource>literal(property.name)
                            .then(
                                    CommandManager.argument("value", property.argumentType)
                                            .executes(context -> {
                                                Object value = context.getArgument("value", Object.class);
                                                property.set(value);
                                                save(context.getSource().getServer());
                                                context.getSource().sendFeedback(() -> property.display().copy().append(" = ").append(property.get().toString()), false);
                                                return 1;
                                            })
                            )
            );
        }

        return builder;
    }
}
