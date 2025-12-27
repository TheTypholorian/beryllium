package net.typho.beryllium

import net.minecraft.client.model.geom.ModelLayerLocation
import net.typho.beryllium.mixin.accessors.ModelLayersAccessor

object ModModelLayers {
    fun init() = Unit

    @JvmField
    val goatSaddle = register("goat", "saddle")

    fun register(path: String, model: String): ModelLayerLocation {
        val loc = ModelLayerLocation(Beryllium.id(path), model)
        ModelLayersAccessor.getAllModels().add(loc)
        return loc
    }
}