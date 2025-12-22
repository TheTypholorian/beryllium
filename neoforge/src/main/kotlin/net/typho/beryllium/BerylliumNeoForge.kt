package net.typho.beryllium

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.typho.beryllium.platform.NeoForgeRegistrar

@Mod(Beryllium.MOD_ID)
class BerylliumNeoForge(bus: IEventBus, modContainer: ModContainer) {
    init {
        Beryllium.init()
        NeoForgeRegistrar.items.register(bus)
        NeoForgeRegistrar.itemComponents.register(bus)
    }
}