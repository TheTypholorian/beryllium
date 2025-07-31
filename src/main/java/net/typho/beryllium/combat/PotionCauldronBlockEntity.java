package net.typho.beryllium.combat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Beryllium;

import java.util.*;

import static net.minecraft.block.LeveledCauldronBlock.LEVEL;

public class PotionCauldronBlockEntity extends BlockEntity {
    public final Map<Level, Float> contents = new LinkedHashMap<>();

    public PotionCauldronBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PotionCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(Beryllium.COMBAT.POTION_CAULDRON_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        contents.clear();

        for (NbtElement element : nbt.getList("contents", NbtList.COMPOUND_TYPE)) {
            NbtCompound compound = ((NbtCompound) element);

            contents.put(new Level(
                    Registries.STATUS_EFFECT.getEntry(Identifier.of(compound.getString("effect"))).orElseThrow(),
                    compound.getInt("amplifier"),
                    compound.getInt("duration")
            ), compound.getFloat("scale"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            NbtCompound compound = new NbtCompound();
            compound.putString("effect", entry.getKey().effect.getIdAsString());
            compound.putInt("amplifier", entry.getKey().amplifier);
            compound.putInt("duration", entry.getKey().duration);
            compound.putFloat("scale", entry.getValue());
        }

        nbt.put("contents", list);
    }

    public boolean addPotion(Iterable<StatusEffectInstance> it, boolean addLevel) {
        assert world != null;
        BlockState state = world.getBlockState(pos);
        int level = state.getOrEmpty(LEVEL).orElse(0);

        if (level > LeveledCauldronBlock.MAX_LEVEL || !state.isOf(Beryllium.COMBAT.POTION_CAULDRON)) {
            return false;
        }

        for (StatusEffectInstance effect : it) {
            contents.compute(new Level(effect.getEffectType(), effect.getAmplifier(), effect.getDuration()), (k, v) -> v == null ? 1 : v + 1);
        }

        markDirty();

        if (addLevel) {
            world.setBlockState(pos, state.with(LEVEL, level + 1));
        }

        return true;
    }

    public PotionContentsComponent takePotion() {
        assert world != null;
        BlockState state = world.getBlockState(pos);
        int level = state.getOrEmpty(LEVEL).orElse(0);

        if (level < 1) {
            return PotionContentsComponent.DEFAULT;
        }

        if (level == 1) {
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
        } else {
            world.setBlockState(pos, state.with(LEVEL, level - 1));
        }

        int red = 0, green = 0, blue = 0;
        float denom = 0;

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            denom += entry.getValue() / level;
            int color = entry.getKey().effect.value().getColor();
            red += (color >> 16) & 0xFF;
            green += (color >> 8) & 0xFF;
            blue += color & 0xFF;
        }

        int color = 0;

        if (denom > 0) {
            red = (int) (red / denom);
            green = (int) (green / denom);
            blue = (int) (blue / denom);

            color = (((red << 8) | green) << 8) | blue;
        }

        List<StatusEffectInstance> effects = new LinkedList<>();

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            effects.add(new StatusEffectInstance(entry.getKey().effect, (int) (entry.getKey().duration * entry.getValue() / level), entry.getKey().amplifier));
        }

        Map<Level, Float> newContents = new LinkedHashMap<>();

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            newContents.put(entry.getKey(), entry.getValue() * (level - 1) / level);
        }

        contents.clear();
        contents.putAll(newContents);
        markDirty();

        return new PotionContentsComponent(
                Optional.of(Beryllium.COMBAT.COCKTAIL),
                Optional.of(color),
                effects
        );
    }

    public record Level(RegistryEntry<StatusEffect> effect, int amplifier, int duration) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Level level = (Level) o;
            return duration == level.duration && amplifier == level.amplifier && Objects.equals(effect, level.effect);
        }

        @Override
        public int hashCode() {
            return Objects.hash(effect, amplifier, duration);
        }
    }
}
