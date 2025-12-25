package net.typho.beryllium.mixin.tridents;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.typho.beryllium.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @SuppressWarnings("unchecked")
    @WrapOperation(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;pickHighestLevel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/component/DataComponentType;)Ljava/util/Optional;"
            )
    )
    private <T> Optional<T> releaseUsing(ItemStack stack, DataComponentType<List<T>> componentType, Operation<Optional<T>> original) {
        return original.call(stack, componentType).or(() -> Optional.of((T) SoundEvents.TRIDENT_RIPTIDE_3));
    }

    @WrapOperation(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"
            )
    )
    private boolean releaseUsing(Player instance, Operation<Boolean> original) {
        if (ModConfig.instance.trident.riptideInRain) {
            return original.call(instance);
        } else {
            return instance.isInWater();
        }
    }
}
