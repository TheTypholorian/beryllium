package net.typho.beryllium.platform.services

import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.Item
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

interface Registrar {
    fun <T : Item> registerItem(id: String, item: Function<Item.Properties, T>): Supplier<T>

    fun <T> registerItemComponent(id: String, builder: UnaryOperator<DataComponentType.Builder<T>>): Supplier<DataComponentType<T>>
}