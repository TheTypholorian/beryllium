package net.typho.beryllium.mixin.exploring;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Blocks;createLeavesBlock(Lnet/minecraft/sound/BlockSoundGroup;)Lnet/minecraft/block/Block;",
                    ordinal = 2
            )
    )
    private static Block createBirchLeaves(BlockSoundGroup soundGroup) {
        return new LeavesBlock(
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.DARK_GREEN)
                        .strength(0.2F)
                        .ticksRandomly()
                        .sounds(soundGroup)
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
                        ParticleUtil.spawnParticle(world, pos, random, Beryllium.EXPLORING.BIRCH_LEAVES_PARTICLE);
                    }
                }
            }
        };
    }
}
