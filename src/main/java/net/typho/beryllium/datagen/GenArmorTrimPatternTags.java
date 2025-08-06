package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.typho.beryllium.combat.Combat;

import java.util.concurrent.CompletableFuture;

public class GenArmorTrimPatternTags extends FabricTagProvider<ArmorTrimPattern> {
    public GenArmorTrimPatternTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.TRIM_PATTERN, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(Combat.RANGED_DAMAGE_TRIMS)
                .add(Identifier.ofVanilla("sentry"));
        getOrCreateTagBuilder(Combat.SAND_VITALITY_TRIMS)
                .add(Identifier.ofVanilla("dune"));
        getOrCreateTagBuilder(Combat.OCEAN_DAMAGE_TRIMS)
                .add(Identifier.ofVanilla("coast"));
        getOrCreateTagBuilder(Combat.OCEAN_SPEED_TRIMS)
                .add(Identifier.ofVanilla("tide"));
        getOrCreateTagBuilder(Combat.JUNGLE_REGEN_TRIMS)
                .add(Identifier.ofVanilla("wild"));
        getOrCreateTagBuilder(Combat.GROUND_ARMOR_TRIMS)
                .add(Identifier.ofVanilla("ward"));
        getOrCreateTagBuilder(Combat.ATTACK_SPEED_TRIMS)
                .add(Identifier.ofVanilla("vex"));
        getOrCreateTagBuilder(Combat.FORTIFY_TRIMS)
                .add(Identifier.ofVanilla("rib"));
        getOrCreateTagBuilder(Combat.GOLD_TRIMS)
                .add(Identifier.ofVanilla("snout"));
        getOrCreateTagBuilder(Combat.SIGHT_TRIMS)
                .add(Identifier.ofVanilla("eye"));
        getOrCreateTagBuilder(Combat.INVISIBLE_TRIMS)
                .add(Identifier.ofVanilla("silence"));
    }
}
