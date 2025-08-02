package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.typho.beryllium.exploring.Exploring;

import java.util.concurrent.CompletableFuture;

public class GenBiomeTags extends FabricTagProvider<Biome> {
    public GenBiomeTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(Exploring.HAS_FIREFLIES)
                .add(BiomeKeys.BIRCH_FOREST)
                .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
                .add(BiomeKeys.SWAMP)
                .add(BiomeKeys.MANGROVE_SWAMP);
        getOrCreateTagBuilder(Exploring.BIRCH_TAG)
                .add(BiomeKeys.BIRCH_FOREST)
                .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
        getOrCreateTagBuilder(Exploring.SPRUCE_TAG)
                .add(BiomeKeys.TAIGA)
                .add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA)
                .add(BiomeKeys.OLD_GROWTH_PINE_TAIGA);
        getOrCreateTagBuilder(Exploring.OAK_TAG)
                .add(BiomeKeys.FOREST);
        getOrCreateTagBuilder(BiomeTags.IS_END)
                .add(Exploring.CORRUPTED_FOREST);
    }
}
