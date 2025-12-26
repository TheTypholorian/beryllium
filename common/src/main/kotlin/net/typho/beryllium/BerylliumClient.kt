package net.typho.beryllium

import net.minecraft.client.model.ShieldModel
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.resources.model.Material

object BerylliumClient {
    @JvmField
    val ravagerShieldSprite = Material(
        Sheets.SHIELD_SHEET,
        Beryllium.id("entity/shield/ravager_shield")
    )
    @JvmField
    val ravagerShieldModel: ShieldModel

    init {
        val mesh = MeshDefinition()
        val root = mesh.root
        root.addOrReplaceChild(
            "plate",
            CubeListBuilder.create().texOffs(0, 0).addBox(-7.0f, -11.0f, -2.0f, 14.0f, 22.0f, 1.0f),
            PartPose.ZERO
        )
        root.addOrReplaceChild(
            "handle",
            CubeListBuilder.create().texOffs(30, 0).addBox(-1.0f, -3.0f, -1.0f, 2.0f, 6.0f, 6.0f),
            PartPose.ZERO
        )
        ravagerShieldModel = ShieldModel(LayerDefinition.create(mesh, 64, 64).bakeRoot())
    }

    fun init() {
    }
}