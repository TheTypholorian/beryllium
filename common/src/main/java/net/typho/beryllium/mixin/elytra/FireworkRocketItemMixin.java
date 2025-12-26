package net.typho.beryllium.mixin.elytra;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.typho.beryllium.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isFallFlying()Z"
            )
    )
    private boolean use(Player instance, Operation<Boolean> original) {
        return ModConfig.instance.elytra.rocketsEnabled && original.call(instance);
    }
}
