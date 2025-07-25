package net.typho.beryllium.exploring;

import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LogBlock extends PillarBlock {
    public LogBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.isSneaking()) {
            ItemStack held = player.getMainHandStack();

            if (held.getItem() instanceof AxeItem) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x != 0 || y != 0 || z != 0) {
                                BlockPos adj = pos.add(x, y, z);

                                if (adj.isWithinDistance(player.getPos(), player.getAttributeValue(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE) * 2)) {
                                    BlockState log = world.getBlockState(adj);

                                    if (log.getBlock() == this) {
                                        if (world.breakBlock(adj, true, player)) {
                                            held.damage(1, player, EquipmentSlot.MAINHAND);

                                            if (!held.isEmpty()) {
                                                log.getBlock().onBreak(world, adj, log, player);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }
}
