package net.typho.beryllium.combat;

import net.minecraft.block.Block;
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
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;

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

        if (world != null && world.isClient) {
            BlockState state = world.getBlockState(pos);
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL_AND_REDRAW);
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
            list.add(compound);
        }

        nbt.put("contents", list);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(pos);
        }
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

        if (addLevel) {
            world.setBlockState(pos, state.with(LEVEL, level + 1));
        }

        markDirty();

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

        int color = getColor();

        List<StatusEffectInstance> effects = new LinkedList<>();

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            effects.add(new StatusEffectInstance(entry.getKey().effect, (int) (entry.getKey().duration * entry.getValue() / level), entry.getKey().amplifier));
        }

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            entry.setValue(entry.getValue() * (level - 1) / level);
        }

        markDirty();

        return new PotionContentsComponent(
                Optional.empty(),
                Optional.of(color),
                effects
        );
    }

    public int getColor() {
        float red = 0, green = 0, blue = 0;
        float denom = 0;

        for (Map.Entry<Level, Float> entry : contents.entrySet()) {
            denom += entry.getValue();
            int color = entry.getKey().effect.value().getColor();
            red += ((color >> 16) & 0xFF) * entry.getValue();
            green += ((color >> 8) & 0xFF) * entry.getValue();
            blue += (color & 0xFF) * entry.getValue();
        }

        int color = -1;

        if (denom > 0) {
            color = ((((int) (red / denom) << 8) | (int) (green / denom)) << 8) | (int) (blue / denom);
        }

        return color;
    }

    @Override
    public @Nullable Object getRenderData() {
        return getColor();
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
