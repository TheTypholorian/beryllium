package net.typho.beryllium.mixin.remove_durability;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin {
    @Shadow
    public abstract <T> Item.Properties component(DataComponentType<T> component, T value);

    @Inject(
            method = "durability",
            at = @At("TAIL")
    )
    private void durability(CallbackInfoReturnable<Boolean> cir) {
        component(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }
}
