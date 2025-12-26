package net.typho.beryllium.pillagers

import net.minecraft.world.item.ShieldItem

class RavagerShieldItem(properties: Properties) : ShieldItem(properties) {
    companion object {
        const val MAX_CHARGE = 32
    }
}