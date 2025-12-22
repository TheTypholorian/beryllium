package net.typho.beryllium

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.component.ChargedProjectiles
import net.typho.beryllium.crossbows.BurstCrossbowItem
import net.typho.beryllium.explosives.DynamiteItem
import net.typho.beryllium.platform.Services

object ModItems {
    fun init() = Unit

    @JvmField
    val burstCrossbow = Services.REGISTRAR.registerItem("burst_crossbow") { properties ->
        BurstCrossbowItem(
            properties
                .stacksTo(1)
                .durability(465)
                .component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)
        )
    }
    @JvmField
    val dynamite = Services.REGISTRAR.registerItem("dynamite") { properties ->
        DynamiteItem(properties)
    }
}