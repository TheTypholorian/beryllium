package net.typho.beryllium.mixin.redstone;

import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Shadow
    public abstract int size();

    @Inject(
            method = "size",
            at = @At("HEAD"),
            cancellable = true
    )
    public void size(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this.getClass() == HopperBlockEntity.class) {
            cir.setReturnValue(5);
        }
    }

    @ModifyConstant(
            method = "<init>",
            constant = @Constant(intValue = 5)
    )
    private int size(int constant) {
        return size();
    }

    /**
     * @author The Typhothanian
     * @reason Faster hoppers
     */
    @Overwrite
    private boolean needsCooldown() {
        return false;
    }

    /**
     * @author The Typhothanian
     * @reason Faster hoppers
     */
    @Overwrite
    private boolean isDisabled() {
        return false;
    }
}
