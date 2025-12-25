package net.typho.beryllium.mixin.end_city;

import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.typho.beryllium.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {
    @ModifyConstant(
            method = "onHitEntity",
            constant = @Constant(intValue = 200)
    )
    private int onHitEntity(int constant) {
        return ModConfig.instance.endCity.shulkerLevitationTicks.get();
    }
}
