package net.typho.beryllium.end_city

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.typho.beryllium.ModBlocks

class GusterBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState
) : BlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(ModBlocks.gusterBlockEntity.get(), pos, state)
}