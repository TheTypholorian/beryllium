package net.typho.beryllium.exploring;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.state.property.Property;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.typho.beryllium.building.Building;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class StoneBrickVariantProcessor extends StructureProcessor {
    public static final StoneBrickVariantProcessor INSTANCE = new StoneBrickVariantProcessor();
    public static final MapCodec<StoneBrickVariantProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static <T extends Comparable<T>> BlockState put(BlockState state, Property<T> prop, BlockState copy) {
        return state.with(prop, copy.get(prop));
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlacementData data) {
        BlockFamily.Variant variant = BlockFamilies.STONE_BRICK.getVariants()
                .entrySet()
                .stream()
                .filter(entry -> current.state().isOf(entry.getValue()))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);

        Random rng = data.getRandom(current.pos());

        if (variant != null && variant != BlockFamily.Variant.CRACKED) {
            Block replace = current.state().getBlock();

            if (rng.nextInt(3) == 0) {
                replace = BlockFamilies.MOSSY_STONE_BRICK.getVariants().getOrDefault(variant, replace);
            } else if (rng.nextInt(3) == 0) {
                replace = Building.CRACKED_STONE_BRICKS.getVariants().getOrDefault(variant, replace);
            }

            BlockState state = replace.getDefaultState();

            for (Property<?> prop : current.state().getProperties()) {
                state = put(state, prop, current.state());
            }

            return new StructureTemplate.StructureBlockInfo(current.pos(), state, current.nbt());
        }

        if (current.state().isOf(Blocks.STONE_BRICKS)) {
            Block replace = current.state().getBlock();

            if (rng.nextInt(3) == 0) {
                replace = BlockFamilies.MOSSY_STONE_BRICK.getBaseBlock();
            } else if (rng.nextInt(3) == 0) {
                replace = Building.CRACKED_STONE_BRICKS.getBaseBlock();
            }

            return new StructureTemplate.StructureBlockInfo(current.pos(), replace.getDefaultState(), current.nbt());
        }

        return super.process(world, pos, pivot, original, current, data);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return Exploring.STONE_BRICK_VARIANT_PROCESSOR;
    }
}
