package net.typho.beryllium.platform

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import net.typho.beryllium.Beryllium
import net.typho.beryllium.platform.services.Registrar
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class NeoForgeRegistrar : Registrar {
    companion object {
        val items: DeferredRegister.Items = DeferredRegister.createItems(Beryllium.MOD_ID)
        val itemComponents: DeferredRegister<DataComponentType<*>> =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Beryllium.MOD_ID)
    }

    override fun <T : Item> registerItem(
        id: String,
        item: Function<Item.Properties, T>
    ): Supplier<T> = items.registerItem(id, item)

    override fun <T> registerItemComponent(
        id: String,
        builder: UnaryOperator<DataComponentType.Builder<T>>
    ): Supplier<DataComponentType<T>> = itemComponents.register(
        id,
        Supplier { builder.apply(DataComponentType.builder()).build() }
    )
}