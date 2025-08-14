package net.typho.beryllium.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.text.Text;
import net.typho.beryllium.client.ClientConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGameOptionPagesMixin {
    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @WrapOperation(
            method = "general",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;build()Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup;",
                    ordinal = 0
            )
    )
    private static OptionGroup general(OptionGroup.Builder instance, Operation<OptionGroup> original) {
        return original.call(instance.add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
                .setName(Text.translatable("options.beryllium.hud_item_tooltips"))
                .setTooltip(Text.translatable("options.beryllium.hud_item_tooltips.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> ((ClientConfig) options).hudItemTooltips().setValue(value), options -> ((ClientConfig) options).hudItemTooltips().getValue())
                .setImpact(OptionImpact.LOW)
                .build()));
    }
}
