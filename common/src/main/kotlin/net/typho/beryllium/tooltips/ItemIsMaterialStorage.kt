package net.typho.beryllium.tooltips

import net.minecraft.world.item.Item

interface ItemIsMaterialStorage {
    fun `beryllium$isMaterial`(): Boolean

    fun `beryllium$setIsMaterial`(value: Boolean)

    companion object {
        @JvmStatic
        fun test(item: Item) = (item as ItemIsMaterialStorage).`beryllium$isMaterial`()

        @JvmStatic
        fun set(item: Item, value: Boolean) = (item as ItemIsMaterialStorage).`beryllium$setIsMaterial`(value)
    }
}