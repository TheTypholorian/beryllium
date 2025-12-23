package net.typho.beryllium.end_city

import net.minecraft.core.BlockBox
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.typho.beryllium.ModParticles

class GusterBlock(properties: Properties) : Block(properties), EntityBlock {
    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(BlockStateProperties.ENABLED, true)
        )
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        level.setBlock(
            pos,
            state.setValue(BlockStateProperties.ENABLED, !state.getValue(BlockStateProperties.ENABLED)),
            UPDATE_CLIENTS
        )
        return InteractionResult.SUCCESS
    }

    override fun newBlockEntity(
        pos: BlockPos,
        state: BlockState
    ) = GusterBlockEntity(pos, state)

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T?>
    ) = BlockEntityTicker<T> { level1, pos1, state1, blockEntity ->
        if (state1.getValue(BlockStateProperties.ENABLED)) {
            val direction = state1.getValue(BlockStateProperties.FACING)
            val sidePos = pos1.relative(direction)
            var endPos = sidePos

            for (i in 0 until LENGTH) {
                endPos = endPos.relative(direction)

                if (!level.getBlockState(endPos).getCollisionShape(level, endPos).isEmpty) {
                    break
                }
            }

            for (entity in level1.getEntities(
                null,
                BlockBox(sidePos, endPos).aabb()
            )) {
                val distance = Mth.clamp(
                    1 - entity.boundingBox.center.distanceTo(Vec3.atCenterOf(pos1)) / LENGTH,
                    0.0,
                    1.0
                ) * 0.25

                entity.addDeltaMovement(
                    Vec3.atLowerCornerOf(direction.normal)
                        .multiply(distance, distance, distance)
                )
            }

            val particlePos = Vec3.atCenterOf(pos1)
                .relative(direction, 0.5)
                .offsetRandom(level.random, 0.5f)

            level.addParticle(
                ModParticles.guster.get(),
                particlePos.x,
                particlePos.y,
                particlePos.z,
                direction.normal.x.toDouble() * 0.5,
                direction.normal.y.toDouble() * 0.5,
                direction.normal.z.toDouble() * 0.5
            )
        }
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(
            BlockStateProperties.FACING,
            rotation.rotate(state.getValue(BlockStateProperties.FACING))
        )
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(BarrelBlock.FACING)))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.ENABLED)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return defaultBlockState()
            .setValue(BlockStateProperties.FACING, context.nearestLookingDirection.opposite)
            .setValue(BlockStateProperties.ENABLED, false)
    }

    companion object {
        const val LENGTH = 16
    }
}