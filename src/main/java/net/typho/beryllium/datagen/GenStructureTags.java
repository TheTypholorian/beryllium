package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.typho.beryllium.exploring.Exploring;

import java.util.concurrent.CompletableFuture;

public class GenStructureTags extends FabricTagProvider<Structure> {
    public GenStructureTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STRUCTURE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(Exploring.ON_BASTION_MAPS)
                .add(StructureKeys.BASTION_REMNANT);
        getOrCreateTagBuilder(Exploring.ON_FORTRESS_MAPS)
                .add(StructureKeys.FORTRESS)
                .addOptionalTag(Identifier.of("betterfortresses", "better_fortresses"));
        getOrCreateTagBuilder(Exploring.SPAWN_KEY)
                .add(StructureKeys.VILLAGE_PLAINS)
                .add(StructureKeys.VILLAGE_DESERT)
                .add(StructureKeys.VILLAGE_SAVANNA)
                .add(StructureKeys.VILLAGE_SNOWY)
                .add(StructureKeys.VILLAGE_TAIGA);
    }
}
