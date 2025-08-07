package net.typho.beryllium.mixin;

import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.objectweb.asm.Opcodes.GETSTATIC;

@Mixin(Util.class)
public class UtilMixin {
    @Redirect(
            method = "getChoiceTypeInternal",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/SharedConstants;isDevelopment:Z",
                    opcode = GETSTATIC
            )
    )
    private static boolean isDevelopment() {
        return false;
    }
}
