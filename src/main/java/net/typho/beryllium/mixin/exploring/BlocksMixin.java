package net.typho.beryllium.mixin.exploring;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.exploring.LogBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Unique
    private static LeavesBlock createLeaves(BlockSoundGroup sounds, Supplier<ParticleEffect> particle) {
        return new LeavesBlock(
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.DARK_GREEN)
                        .strength(0.2F)
                        .ticksRandomly()
                        .sounds(sounds)
                        .nonOpaque()
                        .allowsSpawning(Blocks::canSpawnOnLeaves)
                        .suffocates(Blocks::never)
                        .blockVision(Blocks::never)
                        .burnable()
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .solidBlock(Blocks::never)
        ) {
            @Override
            public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
                super.randomDisplayTick(state, world, pos, random);

                if (random.nextInt(10) == 0) {
                    BlockPos blockPos = pos.down();
                    BlockState blockState = world.getBlockState(blockPos);

                    if (!isFaceFullSquare(blockState.getCollisionShape(world, blockPos), Direction.UP)) {
                        ParticleUtil.spawnParticle(world, pos, random, particle.get());
                    }
                }
            }
        };
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Blocks;createLeavesBlock(Lnet/minecraft/sound/BlockSoundGroup;)Lnet/minecraft/block/Block;",
                    ordinal = 1
            )
    )
    private static Block createSpruceLeaves(BlockSoundGroup sounds) {
        return createLeaves(sounds, () -> Beryllium.EXPLORING.SPRUCE_LEAVES_PARTICLE);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Blocks;createLeavesBlock(Lnet/minecraft/sound/BlockSoundGroup;)Lnet/minecraft/block/Block;",
                    ordinal = 2
            )
    )
    private static Block createBirchLeaves(BlockSoundGroup sounds) {
        return createLeaves(sounds, () -> Beryllium.EXPLORING.BIRCH_LEAVES_PARTICLE);
    }

    /**
     * @author The Typhothanian
     * @reason Falling logs
     */
    @Overwrite
    public static Block createLogBlock(MapColor topMapColor, MapColor sideMapColor) {
        return createLogBlock(topMapColor, sideMapColor, BlockSoundGroup.WOOD);
    }

    /**
     * @author The Typhothanian
     * @reason Falling logs
     */
    @Overwrite
    public static Block createLogBlock(MapColor topMapColor, MapColor sideMapColor, BlockSoundGroup soundGroup) {
        return new LogBlock(
                AbstractBlock.Settings.create()
                        .mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sounds(soundGroup)
                        .burnable()
        );
    }
}
