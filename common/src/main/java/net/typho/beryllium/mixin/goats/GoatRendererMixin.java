package net.typho.beryllium.mixin.goats;

import net.minecraft.client.model.GoatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.goat.Goat;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.ModModelLayers;
import net.typho.beryllium.goats.UnrestrictedSaddleLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoatRenderer.class)
public abstract class GoatRendererMixin extends MobRenderer<Goat, GoatModel<Goat>> {
    public GoatRendererMixin(EntityRendererProvider.Context context, GoatModel<Goat> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {
        addLayer(
                new UnrestrictedSaddleLayer<>(
                        (GoatRenderer) (Object) this,
                        new GoatModel<>(context.bakeLayer(ModModelLayers.goatSaddle)),
                        Beryllium.id("textures/entity/goat/goat_saddle.png")
                )
        );
    }
}
