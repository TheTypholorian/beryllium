package net.typho.beryllium;

import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static net.typho.beryllium.Beryllium.MOD_ID;

public abstract class Module extends Config implements ModInitializer {
    public final String name;
    private boolean enabled = true;

    public Module(String name) {
        super(Identifier.of(MOD_ID, name));
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int defaultPermLevel() {
        return 3;
    }

    @Override
    public @NotNull FileType fileType() {
        return FileType.JSON;
    }

    public Identifier id(String name) {
        return Identifier.of(MOD_ID, this.name + "/" + name);
    }

    public Item item(String id, Item item) {
        return Registry.register(Registries.ITEM, id(id), item);
    }

    public Block block(String id, Block block) {
        return Registry.register(Registries.BLOCK, id(id), block);
    }

    public Block blockWithItem(String id, Block block, Item.Settings settings) {
        Block res = Registry.register(Registries.BLOCK, id(id), block);
        Registry.register(Registries.ITEM, id(id), new BlockItem(res, settings));
        return res;
    }
}
