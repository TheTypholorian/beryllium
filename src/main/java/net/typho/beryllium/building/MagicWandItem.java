package net.typho.beryllium.building;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Module;

import java.util.List;

public class MagicWandItem extends Item {
    public static final ComponentType<BlockPos> COMPONENT_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE, Module.id("magic_wand_component"), ComponentType.<BlockPos>builder().codec(BlockPos.CODEC).build());

    public MagicWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos target = context.getBlockPos().offset(context.getSide());
        BlockPos first = context.getStack().get(COMPONENT_TYPE);

        if (first == null) {
            if (context.getPlayer() != null) {
                ItemStack offStack = context.getPlayer().getOffHandStack();
                context.getStack().set(COMPONENT_TYPE, !offStack.isEmpty() && offStack.getItem() instanceof BlockItem ? target : context.getBlockPos());
            }

            return ActionResult.SUCCESS_NO_ITEM_USED;
        } else {
            if (context.getPlayer() != null) {
                context.getStack().set(COMPONENT_TYPE, null);
                ItemPlacementContext placement = new ItemPlacementContext(context);
                BlockState def;
                ItemStack offStack = context.getPlayer().getOffHandStack();

                if (!offStack.isEmpty() && offStack.getItem() instanceof BlockItem blockItem) {
                    def = blockItem.getBlock().getPlacementState(placement);
                } else {
                    def = Blocks.AIR.getPlacementState(placement);
                    target = context.getBlockPos();
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

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.beryllium.magic_wand"));
    }
}
