package net.typho.beryllium

import net.minecraft.world.level.block.entity.BlockEntityType
import net.typho.beryllium.end_city.GusterBlock
import net.typho.beryllium.end_city.GusterBlockEntity
import net.typho.beryllium.platform.Services

object ModBlocks {
    fun init() = Unit

    @JvmField
    val guster = Services.REGISTRAR.registerBlock("guster") { properties ->
        GusterBlock(properties)
    }
    @JvmField
    val gusterBlockEntity = Services.REGISTRAR.registerBlockEntity(
        "guster",
        BlockEntityType.Builder.of(::GusterBlockEntity, guster.get())
    )
}