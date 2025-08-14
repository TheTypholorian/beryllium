package net.typho.beryllium.mixin.client;

import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.typho.beryllium.client.ClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
    @Inject(
            method = "getOptions",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void getOptions(GameOptions gameOptions, CallbackInfoReturnable<SimpleOption<?>[]> cir) {
        SimpleOption<?>[] array = cir.getReturnValue();
        SimpleOption<?>[] r = new SimpleOption[array.length + 1];
        System.arraycopy(array, 0, r, 0, array.length);
        r[array.length] = ((ClientConfig) gameOptions).hudItemTooltips();
        cir.setReturnValue(r);
    }
}
