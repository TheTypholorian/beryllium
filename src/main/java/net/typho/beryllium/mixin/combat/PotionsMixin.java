package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.typho.beryllium.combat.Combat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Potions.class)
public class PotionsMixin {
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "Lnet/minecraft/potion/Potion;",
                    ordinal = 0
            )
    )
    private static Potion redirectWater(StatusEffectInstance[] effects) {
        return new Potion(new StatusEffectInstance(Combat.WET_EFFECT, 100, 0));
    }
}
