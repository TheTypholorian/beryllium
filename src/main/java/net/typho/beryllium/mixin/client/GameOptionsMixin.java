package net.typho.beryllium.mixin.client;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.typho.beryllium.client.ClientConfig;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
@Implements(@Interface(iface = ClientConfig.class, prefix = "config$"))
public class GameOptionsMixin {
    @Unique
    private SimpleOption<Boolean> hudItemTooltips;
    @Unique
    private SimpleOption<Boolean> compassCoords;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.hudItemTooltips = SimpleOption.ofBoolean(
                "options.beryllium.hud_item_tooltips",
                false,
                value -> {}
        );
        this.compassCoords = SimpleOption.ofBoolean(
                "options.beryllium.compass_coords",
                true,
                value -> {}
        );
    }

    public SimpleOption<Boolean> config$hudItemTooltips() {
        return hudItemTooltips;
    }

    public SimpleOption<Boolean> config$compassCoords() {
        return compassCoords;
    }
}
