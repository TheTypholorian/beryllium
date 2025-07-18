package net.typho.beryllium.mixin.exploring;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyVariable(
            method = "setupSpawn",
            at = @At("HEAD"),
            index = 2,
            argsOnly = true
    )
    private static boolean bonusChest(boolean value) {
        return false;
    }
}
