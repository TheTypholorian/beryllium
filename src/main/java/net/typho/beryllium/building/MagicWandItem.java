package net.typho.beryllium.building;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Module;

import java.util.List;

public class MagicWandItem extends Item {
    public static final ComponentType<BlockPos> COMPONENT_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE, Module.id("magic_wand_component"), ComponentType.<BlockPos>builder().codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC).build());

    public MagicWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos first = context.getStack().get(COMPONENT_TYPE);
        BlockPos target = context.getPlayer().isSneaking() ? context.getBlockPos() : context.getBlockPos().offset(context.getSide());

        if (first == null) {
            if (context.getPlayer() != null) {
                context.getStack().set(COMPONENT_TYPE, target);
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
        BlockPos first = wand.get(COMPONENT_TYPE);
        BlockPos target = player.isSneaking() ? hit.getBlockPos() : hit.getBlockPos().offset(hit.getSide());

        if (first == null) {
            return new BlockBox(target);
        }

        return BlockBox.create(first, target);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.beryllium.magic_wand"));
    }
}
