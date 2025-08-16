package net.typho.beryllium.mixin.client;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.typho.beryllium.client.ClientConfig;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GameOptions.class)
@Implements(@Interface(iface = ClientConfig.class, prefix = "config$"))
public class GameOptionsMixin {
    @Unique
    private final SimpleOption<Boolean> hudItemTooltips = SimpleOption.ofBoolean(
            "options.beryllium.hud_item_tooltips",
            true,
            value -> {}
    );
    @Unique
    private final SimpleOption<Boolean> compassCoords = SimpleOption.ofBoolean(
            "options.beryllium.compass_coords",
            true,
            value -> {}
    );

    public SimpleOption<Boolean> config$hudItemTooltips() {
        return hudItemTooltips;
    }

    public SimpleOption<Boolean> config$compassCoords() {
        return compassCoords;
    }
}
