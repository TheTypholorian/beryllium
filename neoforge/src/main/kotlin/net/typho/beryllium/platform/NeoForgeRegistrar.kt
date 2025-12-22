package net.typho.beryllium.platform

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import net.typho.beryllium.Beryllium
import net.typho.beryllium.platform.services.Registrar
import java.util.function.Function
import java.util.function.Supplier

class NeoForgeRegistrar : Registrar {
    companion object {
        val items: DeferredRegister.Items = DeferredRegister.createItems(Beryllium.MOD_ID)
    }

    override fun <T : Item> registerItem(
        id: String,
        item: Function<Item.Properties, T>
    ): Supplier<T> = items.registerItem(id, item)
}