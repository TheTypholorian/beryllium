package net.typho.beryllium.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin {
    @Shadow
    private Collection<ItemStack> displayItems;

    @Shadow
    private Set<ItemStack> displayItemsSearchTab;

    @Inject(
            method = "buildContents",
            at = @At("TAIL")
    )
    private void buildContents(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        ResourceKey<CreativeModeTab> key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey((CreativeModeTab) (Object) this).orElseThrow();
        List<ItemStack> list = ModItems.getCreativeTabContents(key);

        if (list != null) {
            List<ItemStack> old = displayItems.stream().toList();
            displayItems.clear();
            displayItems.addAll(list);
            displayItems.addAll(old);

            displayItemsSearchTab.addAll(list);
        }
    }
}
