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
        getOrCreateTagBuilder(Combat.SPEED_TRIMS)
                .add(Identifier.ofVanilla("dune"));
        getOrCreateTagBuilder(Combat.DAMAGE_TRIMS)
                .add(Identifier.ofVanilla("coast"));
        getOrCreateTagBuilder(Combat.SWIMMING_SPEED_TRIMS)
                .add(Identifier.ofVanilla("tide"));
        getOrCreateTagBuilder(Combat.REGEN_TRIMS)
                .add(Identifier.ofVanilla("wild"));
        getOrCreateTagBuilder(Combat.ARMOR_TRIMS)
                .add(Identifier.ofVanilla("ward"));
        getOrCreateTagBuilder(Combat.ATTACK_SPEED_TRIMS)
                .add(Identifier.ofVanilla("vex"));
        getOrCreateTagBuilder(Combat.FORTIFY_TRIMS)
                .add(Identifier.ofVanilla("rib"));
        getOrCreateTagBuilder(Combat.FARM_TRIMS)
                .add(Identifier.ofVanilla("snout"));
        getOrCreateTagBuilder(Combat.SIGHT_TRIMS)
                .add(Identifier.ofVanilla("eye"));
        getOrCreateTagBuilder(Combat.LUCK_TRIMS)
                .add(Identifier.ofVanilla("spire"));
        getOrCreateTagBuilder(Combat.MOUNT_REGEN_TRIMS)
                .add(Identifier.ofVanilla("wayfinder"));
        getOrCreateTagBuilder(Combat.MORE_XP_TRIMS)
                .add(Identifier.ofVanilla("raiser"));
        getOrCreateTagBuilder(Combat.CHEAP_XP_TRIMS)
                .add(Identifier.ofVanilla("shaper"));
        getOrCreateTagBuilder(Combat.MOUNT_SPEED_TRIMS)
                .add(Identifier.ofVanilla("host"));
        getOrCreateTagBuilder(Combat.INVISIBLE_TRIMS)
                .add(Identifier.ofVanilla("silence"));
    }
}
