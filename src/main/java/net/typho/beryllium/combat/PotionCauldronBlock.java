package net.typho.beryllium.combat;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;

public class PotionCauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {
    public static final CauldronBehavior BEHAVIOR = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            Item item = stack.getItem();
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));

            if (state.isOf(Beryllium.COMBAT.POTION_CAULDRON)) {
                world.setBlockState(pos, state.with(LEVEL, state.getOrEmpty(LEVEL).orElse(1) + 1));
            } else {
                world.setBlockState(pos, Beryllium.COMBAT.POTION_CAULDRON.getDefaultState());
            }

            ((PotionCauldronBlockEntity) world.getBlockEntity(pos)).addPotion(stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getEffects(), false);
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1, 1);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        return ItemActionResult.success(world.isClient);
    };
    public static final CauldronBehavior.CauldronBehaviorMap BEHAVIOR_MAP = CauldronBehavior.createMap("potion_cauldron");

    public PotionCauldronBlock(Biome.Precipitation precipitation, Settings settings) {
        super(precipitation, BEHAVIOR_MAP, settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PotionCauldronBlockEntity(pos, state);
    }
}
