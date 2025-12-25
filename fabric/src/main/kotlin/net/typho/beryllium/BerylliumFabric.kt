package net.typho.beryllium

import net.fabricmc.api.ModInitializer

object BerylliumFabric : ModInitializer {
    override fun onInitialize() {
        Beryllium.init()
        RemovedThings.reload()
    }
}