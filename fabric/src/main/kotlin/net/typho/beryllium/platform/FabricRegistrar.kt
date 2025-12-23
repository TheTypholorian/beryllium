package net.typho.beryllium.platform

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.beryllium.Beryllium
import net.typho.beryllium.platform.services.Registrar
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class FabricRegistrar : Registrar {
    override fun registerArmorMaterial(id: String, item: Supplier<ArmorMaterial>): Supplier<Holder<ArmorMaterial>> {
        val holder = Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, Beryllium.id(id), item.get())
        return Supplier { holder }
    }

    override fun <T : BlockEntity> registerBlockEntity(
        id: String,
        builder: BlockEntityType.Builder<T>
    ): Supplier<BlockEntityType<T>> {
        val registered = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Beryllium.id(id), builder.build())
        return Supplier { registered }
    }

    override fun <T : Block> registerBlock(id: String, block: Function<BlockBehaviour.Properties, T>): Supplier<T> {
        val registered = Registry.register(BuiltInRegistries.BLOCK, Beryllium.id(id), block.apply(BlockBehaviour.Properties.of()))
        return Supplier { registered }
    }

    override fun <T : Item> registerItem(
        id: String,
        item: Function<Item.Properties, T>
    ): Supplier<T> {
        val applied = item.apply(Item.Properties())
        Registry.register(BuiltInRegistries.ITEM, Beryllium.id(id), applied)
        return Supplier { applied }
    }

    override fun <T> registerItemComponent(
        id: String,
        builder: UnaryOperator<DataComponentType.Builder<T>>
    ): Supplier<DataComponentType<T>> {
        val registered = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Beryllium.id(id),
            builder.apply(DataComponentType.builder()).build()
        )
        return Supplier { registered }
    }

    override fun registerSimpleParticle(id: String, overrideLimiter: Boolean): Supplier<SimpleParticleType> {
        val particle = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            Beryllium.id(id),
            FabricParticleTypes.simple(overrideLimiter)
        )
        return Supplier { particle }
    }
}