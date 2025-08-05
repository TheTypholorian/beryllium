package net.typho.beryllium.building;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class FillingWandItem extends Item {
    public FillingWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos first = context.getStack().get(Building.FILLING_WAND_COMPONENT_TYPE);
        BlockPos target = context.getPlayer().isSneaking() ? context.getBlockPos() : context.getBlockPos().offset(context.getSide());

        if (first == null) {
            if (context.getPlayer() != null) {
                context.getStack().set(Building.FILLING_WAND_COMPONENT_TYPE, target);
            }

            return ActionResult.SUCCESS_NO_ITEM_USED;
        } else {
            if (context.getPlayer() != null) {
                context.getStack().set(Building.FILLING_WAND_COMPONENT_TYPE, null);
                ItemPlacementContext placement = new ItemPlacementContext(context);
                BlockState def;
                ItemStack offStack = context.getPlayer().getOffHandStack();

                if (!offStack.isEmpty() && offStack.getItem() instanceof BlockItem blockItem) {
                    def = blockItem.getBlock().getPlacementState(placement);
                } else {
                    def = Blocks.AIR.getPlacementState(placement);
                }

                BlockBox range = BlockBox.create(first, target);

                if (def != null) {
                    for (BlockPos blockPos : BlockPos.iterate(range.getMinX(), range.getMinY(), range.getMinZ(), range.getMaxX(), range.getMaxY(), range.getMaxZ())) {
                        context.getWorld().setBlockState(blockPos, offStack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(def));
                    }

                    return ActionResult.SUCCESS_NO_ITEM_USED;
                }
            }
        }

        return super.useOnBlock(context);
    }

    public static BlockBox getSelection(PlayerEntity player, ItemStack wand, BlockHitResult hit) {
        BlockPos first = wand.get(Building.FILLING_WAND_COMPONENT_TYPE);
        BlockPos target = player.isSneaking() ? hit.getBlockPos() : hit.getBlockPos().offset(hit.getSide());

        if (first == null) {
            return new BlockBox(target);
        }

        return BlockBox.create(first, target);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.beryllium.building.magic_wand"));
    }
}
