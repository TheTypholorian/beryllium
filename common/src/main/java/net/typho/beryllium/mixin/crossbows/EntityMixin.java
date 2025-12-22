package net.typho.beryllium.mixin.crossbows;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "markHurt",
            at = @At("HEAD")
    )
    private void markHurt(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living) {
            if (living.getUseItem().getItem() instanceof CrossbowItem) {
                living.stopUsingItem();

                if (living instanceof Player player) {
                    player.getCooldowns().addCooldown(living.getUseItem().getItem(), 40);
                }
            }
        }
    }
}
