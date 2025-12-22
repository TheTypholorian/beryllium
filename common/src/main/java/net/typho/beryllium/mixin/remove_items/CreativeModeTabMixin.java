package net.typho.beryllium.mixin.remove_items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.RemovedThings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
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
        displayItems.removeIf(stack -> RemovedThings.items.contains(stack.getItem()));
        displayItemsSearchTab.removeIf(stack -> RemovedThings.items.contains(stack.getItem()));
    }
}
