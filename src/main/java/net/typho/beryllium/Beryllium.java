package net.typho.beryllium;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;

public class Beryllium implements ModInitializer {
    public static final String MOD_ID = "beryllium";

    public static final RegistryEntry<EntityAttribute> GENERIC_HORIZONTAL_DRAG = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "horizontal_drag"), new ClampedEntityAttribute("attribute.beryllium.name.generic.horizontal_drag", 0.91, 0, 1).setTracked(true));
    //public static final RegistryEntry<EntityAttribute> GENERIC_VERTICAL_DRAG = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "vertical_drag"), new ClampedEntityAttribute("attribute.beryllium.name.generic.vertical_drag", 0.098, 0, 1));

    @Override
    public void onInitialize() {
        Enchanting.init();
        Combat.init();
        Building.init();
        Exploring.init();
    }
}
