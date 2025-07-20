package net.typho.beryllium.exploring;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class SusSandProcessor extends StructureProcessor {
    public static final MapCodec<SusSandProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).fieldOf("loot").forGetter(p -> p.loot)
            ).apply(instance, SusSandProcessor::new));

    public final RegistryKey<LootTable> loot;

    public SusSandProcessor(RegistryKey<LootTable> loot) {
        this.loot = loot;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlacementData data) {
        if (current.state().isOf(Blocks.SAND)) {
            Random random = data.getRandom(current.pos());

            if (random.nextInt(15) == 0) {
                boolean exposed = false;

                for (Direction dir : Direction.values()) {
                    if (world.getBlockState(original.pos().offset(dir)).isAir()) {
                        exposed = true;
                        break;
                    }
                }

                if (exposed) {
                    BlockState state = Blocks.SUSPICIOUS_SAND.getDefaultState();
                    NbtCompound nbt = new NbtCompound();
                    nbt.putString("LootTable", loot.getValue().toString());
                    nbt.putLong("LootTableSeed", random.nextLong());
                    return new StructureTemplate.StructureBlockInfo(current.pos(), state, nbt);
                }
            }
        }

        return super.process(world, pos, pivot, original, current, data);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return Exploring.SUS_SAND_PROCESSOR;
    }
}
