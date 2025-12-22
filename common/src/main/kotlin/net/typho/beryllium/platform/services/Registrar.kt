package net.typho.beryllium.platform.services

import net.minecraft.world.item.Item
import java.util.function.Function
import java.util.function.Supplier

interface Registrar {
    fun <T : Item> registerItem(id: String, item: Function<Item.Properties, T>): Supplier<T>
}