package net.typho.beryllium.platform

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.typho.beryllium.Beryllium
import net.typho.beryllium.platform.services.Registrar
import java.util.function.Function
import java.util.function.Supplier

class FabricRegistrar : Registrar {
    override fun <T : Item> registerItem(
        id: String,
        item: Function<Item.Properties, T>
    ): Supplier<T> {
        val applied = item.apply(Item.Properties())
        Registry.register(BuiltInRegistries.ITEM, Beryllium.id(id), applied)
        return Supplier<T> { applied }
    }
}