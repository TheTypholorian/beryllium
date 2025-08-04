package net.typho.beryllium.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.level.storage.LevelStorage;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private @Nullable IntegratedServer server;

    @Inject(
            method = "startIntegratedServer",
            at = @At("TAIL")
    )
    private void ultraDark(LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, boolean newWorld, CallbackInfo ci) {
        if (Beryllium.SERVER_CONFIG.ultraDark.get() && server != null) {
            server.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
            server.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, server);
            server.getGameRules().get(GameRules.REDUCED_DEBUG_INFO).set(true, server);

            for (ServerWorld world : server.getWorlds()) {
                world.setTimeOfDay(18000);
            }

            server.getOverworld().setWeather(0, 20, true, true);
        }
    }
}
