package net.typho.beryllium;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.DynamicTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.util.DynamicTextureReloader;

public class Beryllium implements BerylliumModule {
    public static final RegistryEntry<EntityAttribute> GENERIC_HORIZONTAL_DRAG = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "horizontal_drag"), new ClampedEntityAttribute("attribute.beryllium.name.generic.horizontal_drag", 0.91, 0, 1).setTracked(true));
    //public static final RegistryEntry<EntityAttribute> GENERIC_VERTICAL_DRAG = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "vertical_drag"), new ClampedEntityAttribute("attribute.beryllium.name.generic.vertical_drag", 0.098, 0, 1));

    public static final Enchanting ENCHANTING = new Enchanting();
    public static final Combat COMBAT = new Combat();
    public static final Building BUILDING = new Building();
    public static final Exploring EXPLORING = new Exploring();

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new DynamicTextureReloader());

        ENCHANTING.onInitialize();
        COMBAT.onInitialize();
        BUILDING.onInitialize();
        EXPLORING.onInitialize();
    }
}
