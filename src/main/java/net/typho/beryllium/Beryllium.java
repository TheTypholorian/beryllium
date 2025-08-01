package net.typho.beryllium;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.food.Food;
import net.typho.beryllium.redstone.Redstone;

public class Beryllium implements ModInitializer {
    public static final String MOD_ID = "beryllium";

    public static final Identifier SYNC_GAME_RULE = Identifier.of(MOD_ID, "game_rule_sync");

    public static final BerylliumConfig CONFIG = ConfigApiJava.registerAndLoadConfig(BerylliumConfig::new);
    public static final Exploring EXPLORING = new Exploring("exploring");
    public static final Enchanting ENCHANTING = new Enchanting("enchanting");
    public static final Combat COMBAT = new Combat("combat");
    public static final Building BUILDING = new Building("building");
    public static final Food FOOD = new Food("food");
    public static final Redstone REDSTONE = new Redstone("redstone");

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SyncGameRuleS2CPayload.ID, SyncGameRuleS2CPayload.CODEC);

        ENCHANTING.onInitialize();
        COMBAT.onInitialize();
        EXPLORING.onInitialize();
        BUILDING.onInitialize();
        FOOD.onInitialize();
        REDSTONE.onInitialize();
    }

    public static void syncGameRule(MinecraftServer server, GameRules.Rule<?> rule, String name) {
        SyncGameRuleS2CPayload payload = new SyncGameRuleS2CPayload(name, rule.serialize());

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public record SyncGameRuleS2CPayload(String rule, String value) implements CustomPayload {
        public static final CustomPayload.Id<SyncGameRuleS2CPayload> ID = new CustomPayload.Id<>(SYNC_GAME_RULE);
        public static final PacketCodec<RegistryByteBuf, SyncGameRuleS2CPayload> CODEC = PacketCodec.of(
                (payload, buf) -> {
                    buf.writeString(payload.rule);
                    buf.writeString(payload.value);
                },
                buf -> new SyncGameRuleS2CPayload(
                        buf.readString(),
                        buf.readString()
                )
        );

        @Override
        public Id<SyncGameRuleS2CPayload> getId() {
            return ID;
        }
    }

    public static class BerylliumConfig extends Config {
        public Combat.Config combat = new Combat.Config();
        public Enchanting.Config enchanting = new Enchanting.Config();
        public Exploring.Config exploring = new Exploring.Config();
        public Food.Config food = new Food.Config();
        public Redstone.Config redstone = new Redstone.Config();
        public boolean durabilityRemoval = true;

        public BerylliumConfig() {
            super(Identifier.of(MOD_ID, "common"));
        }
    }
}
