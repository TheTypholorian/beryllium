package net.typho.beryllium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.config.Feature;
import net.typho.beryllium.config.ServerConfig;
import net.typho.beryllium.config.SyncServerConfigS2C;
import net.typho.beryllium.util.Constructor;

public class Beryllium implements ModInitializer {
    public static final String MOD_ID = "beryllium";
    public static final Constructor BASE_CONSTRUCTOR = new Constructor();
    public static final Identifier SYNC_SERVER_CONFIG_ID = BASE_CONSTRUCTOR.id("sync_server_config");

    public static final Constructor COMBAT_CONSTRUCTOR = new Constructor("combat");
    public static final Constructor CHALLENGES_CONSTRUCTOR = new Constructor("challenges");
    public static final Constructor ARMOR_CONSTRUCTOR = new Constructor("armor");
    public static final Constructor BUILDING_CONSTRUCTOR = new Constructor("building");
    public static final Constructor ENCHANTING_CONSTRUCTOR = new Constructor("enchanting");
    public static final Constructor EXPLORING_CONSTRUCTOR = new Constructor("exploring");
    public static final Constructor FOOD_CONSTRUCTOR = new Constructor("food");
    public static final Constructor REDSTONE_CONSTRUCTOR = new Constructor("redstone");

    @Override
    public void onInitialize() {
        ModContainer mod = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
        PayloadTypeRegistry.playS2C().register(SyncServerConfigS2C.ID, SyncServerConfigS2C.PACKET_CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            for (Feature<?> feature : ServerConfig.ALL_FEATURES.values()) {
                sender.sendPacket(new SyncServerConfigS2C(feature));
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) -> {
            dispatcher.register(ServerConfig.command(CommandManager.literal(Beryllium.MOD_ID)
                    .executes(context -> {
                        context.getSource().sendFeedback(() -> Text.literal(mod.getMetadata().getName() + " v" + FabricLoader.getInstance().getModContainer(Beryllium.MOD_ID).orElseThrow().getMetadata().getVersion()), false);
                        return 1;
                    })));
        });
        ResourceManagerHelper.registerBuiltinResourcePack(BASE_CONSTRUCTOR.id("looting_axes"), mod, Text.translatable("pack.name.beryllium.looting_axes"), ResourcePackActivationType.DEFAULT_ENABLED);
        ResourceManagerHelper.registerBuiltinResourcePack(BASE_CONSTRUCTOR.id("flat_models"), mod, Text.translatable("pack.name.beryllium.flat_models"), ResourcePackActivationType.NORMAL);
        ResourceManagerHelper.registerBuiltinResourcePack(BASE_CONSTRUCTOR.id("bare_beryllium"), mod, Text.translatable("pack.name.beryllium.bare_beryllium"), ResourcePackActivationType.NORMAL);
    }
}
