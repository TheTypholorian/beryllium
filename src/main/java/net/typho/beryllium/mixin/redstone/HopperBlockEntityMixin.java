package net.typho.beryllium.mixin.redstone;

import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
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
