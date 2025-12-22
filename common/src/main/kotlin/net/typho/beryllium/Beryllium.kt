package net.typho.beryllium

import net.minecraft.resources.ResourceLocation

object Beryllium {
    const val MOD_ID = "beryllium"

    fun init() {
        ModItems.init()
    }

    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}