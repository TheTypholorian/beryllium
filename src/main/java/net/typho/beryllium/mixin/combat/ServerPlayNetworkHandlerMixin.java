package net.typho.beryllium.mixin.combat;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.typho.beryllium.combat.Combat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @ModifyConstant(
            method = "onVehicleMove",
            constant = @Constant(doubleValue = 0.0625)
    )
    private double vehicleMoveCap(double constant, @Local Entity entity) {
        return constant * Math.sqrt(Combat.bonusMountSpeed(entity));
    }
}
