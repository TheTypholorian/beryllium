package net.typho.beryllium.exploring;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.gen.structure.Structure;
import net.typho.beryllium.Beryllium;

import java.util.List;
import java.util.Optional;

public class ExplorationCompassLootFunction extends ConditionalLootFunction {
    public static final MapCodec<ExplorationCompassLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> addConditionsField(instance).and(instance.group(
            TagKey.unprefixedCodec(RegistryKeys.STRUCTURE).fieldOf("destination").forGetter((function) -> function.destination),
            Codecs.rangedInt(0, Integer.MAX_VALUE).optionalFieldOf("search_radius", 50).forGetter(def -> def.searchRadius),
            Codec.BOOL.optionalFieldOf("skip_existing_chunks", true).forGetter(def -> def.skipExistingChunks)
    )).apply(instance, ExplorationCompassLootFunction::new));

    private final TagKey<Structure> destination;
    private final int searchRadius;
    private final boolean skipExistingChunks;

    public ExplorationCompassLootFunction(List<LootCondition> conditions, TagKey<Structure> destination, int searchRadius, boolean skipExistingChunks) {
        super(conditions);
        this.destination = destination;
        this.searchRadius = searchRadius;
        this.skipExistingChunks = skipExistingChunks;
    }

    @Override
    public LootFunctionType<? extends ConditionalLootFunction> getType() {
        return Beryllium.EXPLORING.EXPLORATION_COMPASS;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (stack.isOf(Items.COMPASS)) {
            Entity origin = context.get(LootContextParameters.THIS_ENTITY);

            if (origin != null) {
                ServerWorld serverWorld = context.getWorld();
                BlockPos blockPos = serverWorld.locateStructure(destination, origin.getBlockPos(), searchRadius, skipExistingChunks);

                if (blockPos != null) {
                    stack.set(DataComponentTypes.LODESTONE_TRACKER, new LodestoneTrackerComponent(Optional.of(GlobalPos.create(serverWorld.getRegistryKey(), blockPos)), false));
                }
            }
        }

        return stack;
    }

    public static class Builder extends ConditionalLootFunction.Builder<Builder> {
        private TagKey<Structure> destination;
        private int searchRadius;
        private boolean skipExistingChunks;

        public Builder() {
            this.searchRadius = 50;
            this.skipExistingChunks = true;
        }

        protected Builder getThisBuilder() {
            return this;
        }

        public Builder withDestination(TagKey<Structure> destination) {
            this.destination = destination;
            return this;
        }

        public Builder searchRadius(int searchRadius) {
            this.searchRadius = searchRadius;
            return this;
        }

        public Builder withSkipExistingChunks(boolean skipExistingChunks) {
            this.skipExistingChunks = skipExistingChunks;
            return this;
        }

        public LootFunction build() {
            return new ExplorationCompassLootFunction(getConditions(), destination, searchRadius, skipExistingChunks);
        }
    }
}
