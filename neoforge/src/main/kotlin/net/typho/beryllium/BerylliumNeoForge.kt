package net.typho.beryllium

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(Beryllium.MOD_ID)
class BerylliumNeoForge(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        Beryllium.init()
    }
}