package net.typho.beryllium.mixin.goats;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.typho.beryllium.ModModelLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {
    @Inject(
            method = "createRoots",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
                    remap = false
            )
    )
    private static void createRoots(
            CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir,
            @Local ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder
    ) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        CubeDeformation deformation = new CubeDeformation(0.5f);
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(2, 61)
                        .addBox("right ear", -6.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F, deformation)
                        .texOffs(2, 61)
                        .mirror()
                        .addBox("left ear", 2.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F, deformation)
                        .texOffs(23, 52)
                        .addBox("goatee", -0.5F, -3.0F, -14.0F, 0.0F, 7.0F, 5.0F, deformation),
                PartPose.offset(1.0F, 14.0F, 0.0F)
        );
        partDefinition2.addOrReplaceChild(
                "left_horn", CubeListBuilder.create()
                        .texOffs(12, 55)
                        .addBox(-0.01F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F, deformation),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partDefinition2.addOrReplaceChild(
                "right_horn", CubeListBuilder.create()
                        .texOffs(12, 55)
                        .addBox(-2.99F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F, deformation),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partDefinition2.addOrReplaceChild(
                "nose",
                CubeListBuilder.create()
                        .texOffs(34, 46)
                        .addBox(-3.0F, -4.0F, -8.0F, 5.0F, 7.0F, 10.0F, deformation),
                PartPose.offsetAndRotation(0.0F, -8.0F, -8.0F, 0.9599F, 0.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(1, 1)
                        .addBox(-4.0F, -17.0F, -7.0F, 9.0F, 11.0F, 16.0F, deformation)
                        .texOffs(0, 28)
                        .addBox(-5.0F, -18.0F, -8.0F, 11.0F, 14.0F, 11.0F, deformation),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "left_hind_leg", CubeListBuilder.create()
                        .texOffs(36, 29)
                        .addBox(0.0F, 4.0F, 0.0F, 3.0F, 6.0F, 3.0F, deformation),
                PartPose.offset(1.0F, 14.0F, 4.0F)
        );
        partDefinition.addOrReplaceChild(
                "right_hind_leg", CubeListBuilder.create()
                        .texOffs(49, 29)
                        .addBox(0.0F, 4.0F, 0.0F, 3.0F, 6.0F, 3.0F, deformation),
                PartPose.offset(-3.0F, 14.0F, 4.0F)
        );
        partDefinition.addOrReplaceChild(
                "left_front_leg", CubeListBuilder.create()
                        .texOffs(49, 2)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, deformation),
                PartPose.offset(1.0F, 14.0F, -6.0F)
        );
        partDefinition.addOrReplaceChild(
                "right_front_leg", CubeListBuilder.create()
                        .texOffs(35, 2)
                        .addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, deformation),
                PartPose.offset(-3.0F, 14.0F, -6.0F)
        );

        builder.put(ModModelLayers.goatSaddle, LayerDefinition.create(meshDefinition, 64, 64));
    }
}
