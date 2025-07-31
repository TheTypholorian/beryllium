package net.typho.beryllium.exploring;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.typho.beryllium.Beryllium;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class NetherRegion extends Region {
    public NetherRegion(Identifier name, RegionType type, int weight) {
        super(name, type, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        addBiome(mapper, MultiNoiseUtil.createNoiseHypercube(-0.7F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.575F), Beryllium.EXPLORING.BONE_FOREST);
    }
}
