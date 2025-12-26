package net.typho.beryllium

import com.mojang.serialization.Codec
import net.minecraft.network.codec.ByteBufCodecs
import net.typho.beryllium.platform.Services

object ModItemComponents {
    fun init() = Unit

    @JvmField
    val ravagerShieldCharge = Services.REGISTRAR.registerItemComponent<Int>("ravager_shield_charge") { builder ->
        builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    }
}