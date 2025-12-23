package net.typho.beryllium

import net.typho.beryllium.platform.Services

object ModParticles {
    fun init() = Unit

    @JvmField
    val guster = Services.REGISTRAR.registerSimpleParticle("guster", true)
}