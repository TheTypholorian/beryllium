package net.typho.beryllium.mixin.exploring;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.level.ServerWorldProperties;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    /**
     * @author The Typhothanian
     * @reason Spawn in Village
     */
    @Inject(
            method = "setupSpawn",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void setupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
        if (Beryllium.CONFIG.exploring.spawnInVillage) {
            BlockPos spawn = world.locateStructure(Beryllium.EXPLORING.SPAWN_KEY, new BlockPos(0, 0, 0), 500, false);

            if (spawn != null) {
                worldProperties.setSpawnPos(new BlockPos(spawn.getX(), world.getTopY(Heightmap.Type.WORLD_SURFACE, spawn.getX(), spawn.getZ()), spawn.getZ()), 0);
                ci.cancel();
            }
        }
    }
}
