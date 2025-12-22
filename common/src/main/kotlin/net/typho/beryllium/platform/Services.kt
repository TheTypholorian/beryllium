package net.typho.beryllium.platform

import net.typho.beryllium.platform.services.PlatformHelper
import net.typho.beryllium.platform.services.Registrar
import java.util.*

object Services {
    val PLATFORM = load(PlatformHelper::class.java)
    val REGISTRAR = load(Registrar::class.java)

    fun <T> load(clazz: Class<T>): T {
        val loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow {
                IllegalStateException("Failed to load service for ${clazz.name}")
            }
        return loadedService
    }
}