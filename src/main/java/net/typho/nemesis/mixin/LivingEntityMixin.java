package net.typho.nemesis.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyConstant(
            method = "travel",
            constant = @Constant(floatValue = 0.91f)
    )
    private float getHDrag(float constant) {
        LivingEntity living = (LivingEntity) (Object) this;
        return (float) living.getAttributeValue(Nemesis.GENERIC_HORIZONTAL_DRAG);
    }

    @Inject(
            method = "createLivingAttributes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(Nemesis.GENERIC_HORIZONTAL_DRAG));
    }

    /*
    @Redirect(
            method = "travel(Lnet/minecraft/util/math/Vec3d;)V",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/LivingEntity;hasNoDrag()Z"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"
            )
    )
    private void getVDrag(LivingEntity self, double x, double y, double z) {
        double drag = self.getAttributeValue(Nemesis.GENERIC_VERTICAL_DRAG);

        if (!(self instanceof Flutterer)) {
            y /= 0.98f;
            y *= drag;
        }

        self.setVelocity(x, y, z);
    }
     */
}
