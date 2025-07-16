package net.typho.beryllium.exploring;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.BerylliumModule;

public class Exploring implements BerylliumModule {
    public static final Item METAL_DETECTOR_ITEM = Registry.register(Registries.ITEM, Identifier.of(Beryllium.MOD_ID, "metal_detector"), new MetalDetectorItem(new Item.Settings()));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    entries.addAfter(Items.COMPASS, METAL_DETECTOR_ITEM);
                });
    }
}
