package net.typho.beryllium;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.typho.beryllium.config.*;
import net.typho.beryllium.util.Constructor;

public class Beryllium implements ModInitializer {
    public static final String MOD_ID = "beryllium";
    public static final Constructor CONSTRUCTOR = new Constructor();

    public static final Identifier SYNC_SERVER_CONFIG_ID = CONSTRUCTOR.id("sync_server_config");

    public static final BerylliumConfig CONFIG = ConfigApiJava.registerAndLoadConfig(BerylliumConfig::new);
    public static ServerConfig SERVER_CONFIG = new ServerConfig();

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SyncServerConfigS2C.ID, SyncServerConfigS2C.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sender.sendPacket(new SyncServerConfigS2C()));
        ArgumentTypeRegistry.registerArgumentType(CONSTRUCTOR.id("config_key"), KeyArgumentType.class, new KeyArgumentSerializer(SERVER_CONFIG));
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) -> {
            dispatcher.register(
                    CommandManager.literal(MOD_ID)
                            .executes(context -> {
                                context.getSource().sendFeedback(() -> Text.literal("Beryllium v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion()), false);
                                return 1;
                            })
                            .then(
                                    CommandManager.literal("config")
                                            .executes(context -> {
                                                context.getSource().sendFeedback(() -> SERVER_CONFIG.print(), false);
                                                return 1;
                                            })
                                            .then(
                                                    LiteralArgumentBuilder.<ServerCommandSource>literal("get")
                                                            .then(
                                                                    CommandManager.argument("key", SERVER_CONFIG.keyArgumentType())
                                                                            .executes(context -> {
                                                                                String key = context.getArgument("key", String.class);
                                                                                Property<?> property = SERVER_CONFIG.properties.get(key);

                                                                                if (property == null) {
                                                                                    context.getSource().sendFeedback(() -> Text.literal("No config value " + key).styled(style -> style.withColor(Formatting.RED)), false);
                                                                                    return 0;
                                                                                } else {
                                                                                    context.getSource().sendFeedback(() -> property.display().copy().append(" = ").append(property.get().toString()), false);
                                                                                    return 1;
                                                                                }
                                                                            })
                                                            )
                                            )
                                            .then(
                                                    SERVER_CONFIG.setCommandBuilder()
                                            )
                                            .build()
                            )
            );
        });
        ResourceManagerHelper.registerBuiltinResourcePack(CONSTRUCTOR.id("looting_axes"), FabricLoader.getInstance().getModContainer(MOD_ID).get(), Text.translatable("pack.name.beryllium.looting_axes"), ResourcePackActivationType.DEFAULT_ENABLED);
        ResourceManagerHelper.registerBuiltinResourcePack(CONSTRUCTOR.id("flat_models"), FabricLoader.getInstance().getModContainer(MOD_ID).get(), Text.translatable("pack.name.beryllium.flat_models"), ResourcePackActivationType.NORMAL);
    }
}
