package net.typho.beryllium.mixin.exploring;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.structure.Structure;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Shadow public abstract @Nullable BlockPos locateStructure(TagKey<Structure> structureTag, BlockPos pos, int radius, boolean skipReferencedStructures);

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Override
    public BlockPos getSpawnPos() {
        BlockPos spawn = locateStructure(Beryllium.EXPLORING.SPAWN_KEY, super.getSpawnPos(), 500, false);

        if (spawn != null) {
            properties.setSpawnPos(new BlockPos(spawn.getX(), getTopY(Heightmap.Type.WORLD_SURFACE, spawn.getX(), spawn.getZ()), spawn.getZ()), 0);
        }

        return properties.getSpawnPos();
    }
}
