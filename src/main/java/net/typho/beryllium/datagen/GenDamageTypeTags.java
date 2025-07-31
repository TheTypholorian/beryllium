package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.typho.beryllium.Beryllium;

import java.util.concurrent.CompletableFuture;

public class GenDamageTypeTags extends FabricTagProvider<DamageType> {
    public GenDamageTypeTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(TagKey.of(registryRef, Beryllium.EXPLORING.id("kinetic")))
                .add(DamageTypes.FLY_INTO_WALL);
    }
}
