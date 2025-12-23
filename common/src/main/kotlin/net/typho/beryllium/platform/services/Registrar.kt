package net.typho.beryllium.platform.services

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

interface Registrar {
    fun registerArmorMaterial(id: String, item: Supplier<ArmorMaterial>): Supplier<Holder<ArmorMaterial>>

    fun <T : BlockEntity> registerBlockEntity(id: String, builder: BlockEntityType.Builder<T>): Supplier<BlockEntityType<T>>

    fun <T : Block> registerBlock(id: String, block: Function<BlockBehaviour.Properties, T>): Supplier<T>

    fun <T : Item> registerItem(id: String, item: Function<Item.Properties, T>): Supplier<T>

    fun <T> registerItemComponent(id: String, builder: UnaryOperator<DataComponentType.Builder<T>>): Supplier<DataComponentType<T>>

    fun registerSimpleParticle(id: String, overrideLimiter: Boolean): Supplier<SimpleParticleType>
}