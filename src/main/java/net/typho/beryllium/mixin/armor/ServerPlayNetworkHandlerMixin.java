package net.typho.beryllium.mixin.armor;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.typho.beryllium.armor.Armor;
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
        return constant * Math.sqrt(Armor.bonusMountSpeed(entity));
    }
}
