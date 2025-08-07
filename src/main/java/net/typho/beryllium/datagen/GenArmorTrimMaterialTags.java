package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.typho.beryllium.combat.Combat;

import java.util.concurrent.CompletableFuture;

public class GenArmorTrimMaterialTags extends FabricTagProvider<ArmorTrimMaterial> {
    public GenArmorTrimMaterialTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.TRIM_MATERIAL, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(Combat.SWEEPING_MATERIALS)
                .add(Identifier.ofVanilla("amethyst"));
        getOrCreateTagBuilder(Combat.REACH_MATERIALS)
                .add(Identifier.ofVanilla("copper"));
        getOrCreateTagBuilder(Combat.ENHANCE_MATERIALS)
                .add(Identifier.ofVanilla("diamond"));
        getOrCreateTagBuilder(Combat.DISCOUNT_MATERIALS)
                .add(Identifier.ofVanilla("emerald"));
        getOrCreateTagBuilder(Combat.ENCHANTABILITY_MATERIALS)
                .add(Identifier.ofVanilla("gold"));
        getOrCreateTagBuilder(Combat.KNOCKBACK_MATERIALS)
                .add(Identifier.ofVanilla("iron"));
        getOrCreateTagBuilder(Combat.JUMP_MATERIALS)
                .add(Identifier.ofVanilla("lapis"));
        getOrCreateTagBuilder(Combat.EFFICIENCY_MATERIALS)
                .add(Identifier.ofVanilla("quartz"));
        getOrCreateTagBuilder(Combat.ARMOR_MATERIALS)
                .add(Identifier.ofVanilla("netherite"));
        getOrCreateTagBuilder(Combat.SATURATION_MATERIALS)
                .add(Identifier.ofVanilla("redstone"));
    }
}
