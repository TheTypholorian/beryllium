package net.typho.beryllium.exploring;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.typho.beryllium.Beryllium;

public class ContainerContentsProcessor extends StructureProcessor {
    public static final MapCodec<ContainerContentsProcessor> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).fieldOf("loot").forGetter(p -> p.loot),
                    RegistryKey.createCodec(RegistryKeys.BLOCK).fieldOf("target").forGetter(p -> p.target)
            ).apply(inst, ContainerContentsProcessor::new)
    );

    public final RegistryKey<LootTable> loot;
    public final RegistryKey<Block> target;

    public ContainerContentsProcessor(RegistryKey<LootTable> loot, RegistryKey<Block> target) {
        this.loot = loot;
        this.target = target;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current, StructurePlacementData data) {
        if (current.state().isOf(Registries.BLOCK.get(target))) {
            NbtCompound tag = new NbtCompound();
            tag.putString("LootTable", loot.getValue().toString());
            tag.putLong("LootTableSeed", data.getRandom(pos).nextLong());

            return new StructureTemplate.StructureBlockInfo(
                    current.pos(),
                    current.state(),
                    tag
            );
        }

        return current;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return Beryllium.EXPLORING.CONTAINER_CONTENTS_PROCESSOR;
    }
}
