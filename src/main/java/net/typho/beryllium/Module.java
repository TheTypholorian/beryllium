package net.typho.beryllium;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface Module extends ModInitializer {
    String MOD_ID = "beryllium";

    static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    static Item item(String id, Item item) {
        return Registry.register(Registries.ITEM, id(id), item);
    }

    static Block block(String id, Block block) {
        return Registry.register(Registries.BLOCK, id(id), block);
    }
}
