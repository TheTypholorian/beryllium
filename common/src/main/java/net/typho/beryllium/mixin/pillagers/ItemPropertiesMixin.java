package net.typho.beryllium.mixin.pillagers;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.typho.beryllium.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemProperties.class)
public abstract class ItemPropertiesMixin {
    @Shadow
    private static void register(Item item, ResourceLocation name, ClampedItemPropertyFunction property) {
    }

    @Inject(
            method = "register",
            at = @At("HEAD")
    )
    private static void register(Item item, ResourceLocation name, ClampedItemPropertyFunction property, CallbackInfo ci) {
        if (item == Items.SHIELD) {
            register(ModItems.ravagerShield.get(), name, property);
        }
    }
}
