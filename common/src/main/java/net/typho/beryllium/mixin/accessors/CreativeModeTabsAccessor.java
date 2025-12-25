package net.typho.beryllium.mixin.accessors;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeTabs.class)
public interface CreativeModeTabsAccessor {
    @Accessor("COMBAT")
    static ResourceKey<CreativeModeTab> getCombatTab() {
        return null;
    }

    @Accessor("TOOLS_AND_UTILITIES")
    static ResourceKey<CreativeModeTab> getToolsTab() {
        return null;
    }
}
