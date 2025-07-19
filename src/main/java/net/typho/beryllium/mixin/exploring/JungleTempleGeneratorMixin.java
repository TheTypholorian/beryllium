package net.typho.beryllium.mixin.exploring;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.structure.JungleTempleGenerator;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.typho.beryllium.Module;
import net.typho.beryllium.exploring.ContainerContentsProcessor;
import net.typho.beryllium.exploring.StoneBrickVariantProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(JungleTempleGenerator.class)
public abstract class JungleTempleGeneratorMixin extends ShiftableStructurePiece {
    protected JungleTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    protected JungleTempleGeneratorMixin(StructurePieceType structurePieceType, NbtCompound nbtCompound) {
        super(structurePieceType, nbtCompound);
    }

    /**
     * @author The Typhothanian
     * @reason Custom jungle temple
     */
    @Overwrite
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        world.setBlockState(pivot, Blocks.BEDROCK.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);

        if (adjustToAverageHeight(world, chunkBox, -6)) {
            Objects.requireNonNull(world.getServer())
                    .getStructureTemplateManager()
                    .getTemplate(Module.id("jungle_pyramid"))
                    .orElseThrow()
                    .place(
                        world,
                        new BlockPos(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ()),
                        pivot,
                        new StructurePlacementData()
                                .setMirror(BlockMirror.NONE)
                                .setRotation(BlockRotation.random(random))
                                .addProcessor(new StoneBrickVariantProcessor())
                                .addProcessor(new ContainerContentsProcessor(LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST, Registries.BLOCK.getKey(Blocks.DISPENSER).orElseThrow()))
                                .addProcessor(new ContainerContentsProcessor(LootTables.JUNGLE_TEMPLE_CHEST, Registries.BLOCK.getKey(Blocks.CHEST).orElseThrow()))
                                .setRandom(random),
                        random,
                        2
                );
        }
    }
}
