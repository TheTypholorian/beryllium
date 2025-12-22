package net.typho.beryllium

import net.typho.beryllium.platform.Services
import net.typho.beryllium.tiers.Tier

object ModItemComponents {
    fun init() = Unit

    @JvmField
    val tier = Services.REGISTRAR.registerItemComponent<Tier>("tier") { builder ->
        builder.persistent(Tier.codec)
            .networkSynchronized(Tier.packetCodec)
            .cacheEncoding()
    }
}