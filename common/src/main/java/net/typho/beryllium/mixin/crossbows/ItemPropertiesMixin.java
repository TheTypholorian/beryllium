package net.typho.beryllium.mixin.crossbows;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.typho.beryllium.Beryllium;
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
        if (item == Items.CROSSBOW) {
            register(ModItems.getBurstCrossbow().get(), name, property);
        }
    }

    static {
        register(
                Items.CROSSBOW,
                Beryllium.id("wind_charge"),
                (stack, level, entity, i) -> {
                    ChargedProjectiles projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
                    return projectiles != null && projectiles.contains(Items.WIND_CHARGE) ? 1.0F : 0.0F;
                }
        );
    }
}
