package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.typho.beryllium.armor.Armor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyVariable(
            method = "move",
            at = @At("HEAD"),
            index = 2,
            argsOnly = true
    )
    private Vec3d movement(Vec3d value) {
        return value.multiply(1 + Armor.bonusMountSpeed((Entity) (Object) this));
    }
}
