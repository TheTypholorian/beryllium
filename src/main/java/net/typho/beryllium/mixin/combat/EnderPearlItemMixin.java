package net.typho.beryllium.mixin.combat;

import net.minecraft.item.EnderPearlItem;
import net.typho.beryllium.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin {
    @ModifyConstant(
            method = "use",
            constant = @Constant(intValue = 20)
    )
    private int getCooldown(int original) {
        return Config.enderPearlCooldown.get();
    }

    @ModifyConstant(
            method = "use",
            constant = @Constant(floatValue = 1.5f)
    )
    private float getVelocity(float constant) {
        return Config.enderPearlSpeed.get();
    }
}