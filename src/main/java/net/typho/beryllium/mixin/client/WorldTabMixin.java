package net.typho.beryllium.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldScreenOptionGrid;
import net.minecraft.text.Text;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screen.world.CreateWorldScreen$WorldTab")
public class WorldTabMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$Builder;add(Lnet/minecraft/text/Text;Ljava/util/function/BooleanSupplier;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/world/WorldScreenOptionGrid$OptionBuilder;",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void ultraDark(CreateWorldScreen screen, CallbackInfo ci, @Local WorldScreenOptionGrid.Builder builder) {
        builder.add(Text.translatable("selectWorld.beryllium.ultraDark"), Beryllium.SERVER_CONFIG.ultraDark::get, Beryllium.SERVER_CONFIG.ultraDark::set)
                .tooltip(Text.translatable("selectWorld.beryllium.ultraDark.info"));
    }
}
