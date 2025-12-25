package net.typho.beryllium

import net.minecraft.resources.ResourceLocation

object Beryllium {
    const val MOD_ID = "beryllium"

    fun init() {
        ModConfig.init()
        ModBlocks.init()
        ModEntities.init()
        ModItemComponents.init()
        ModItems.init()
        ModParticles.init()
    }

    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}