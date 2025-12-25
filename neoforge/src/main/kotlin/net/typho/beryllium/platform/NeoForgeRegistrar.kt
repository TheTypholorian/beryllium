package net.typho.beryllium.platform

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredRegister
import net.typho.beryllium.Beryllium
import net.typho.beryllium.platform.services.Registrar
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class NeoForgeRegistrar : Registrar {
    companion object {
        val blocks: DeferredRegister.Blocks = DeferredRegister.createBlocks(Beryllium.MOD_ID)
        val items: DeferredRegister.Items = DeferredRegister.createItems(Beryllium.MOD_ID)
        val itemComponents: DeferredRegister<DataComponentType<*>> =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Beryllium.MOD_ID)
    }

    override fun registerArmorMaterial(
        id: String,
        item: Supplier<ArmorMaterial>
    ): Supplier<Holder<ArmorMaterial>> {
        TODO("Not yet implemented")
    }

    override fun <T : BlockEntity> registerBlockEntity(
        id: String,
        builder: BlockEntityType.Builder<T>
    ): Supplier<BlockEntityType<T>> {
        TODO("Not yet implemented")
    }

    override fun <T : Block> registerBlock(
        id: String,
        block: Function<BlockBehaviour.Properties, T>
    ): Supplier<T> = blocks.registerBlock(id, block)

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

    override fun registerSimpleParticle(
        id: String,
        overrideLimiter: Boolean
    ): Supplier<SimpleParticleType> {
        TODO("Not yet implemented")
    }
}