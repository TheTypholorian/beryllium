package net.typho.beryllium.mixin.combat;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.PotionCauldronBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleItemMixin extends Item {
    public GlassBottleItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        Hand hand = context.getHand();
        BlockState blockState = world.getBlockState(pos);

        if (blockState.isOf(Beryllium.COMBAT.POTION_CAULDRON)) {
            if (!world.isClient) {
                PotionContentsComponent contents = ((PotionCauldronBlockEntity) world.getBlockEntity(pos)).takePotion();

                if (contents.hasEffects()) {
                    ItemStack potion = new ItemStack(Items.POTION);
                    potion.set(DataComponentTypes.POTION_CONTENTS, contents);
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, potion));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));

                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1, 1);
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }
}
