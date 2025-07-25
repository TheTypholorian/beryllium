package net.typho.beryllium.mixin.food;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.*;

@Mixin(SugarCaneBlock.class)
@Implements(@Interface(iface = Fertilizable.class, prefix = "fert$"))
public class SugarCaneBlockMixin {
    @Shadow @Final public static IntProperty AGE;

    public boolean fert$isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean fert$canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void fert$grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int y = pos.getY();
        BlockState top = state;

        while (true) {
            BlockState check = world.getBlockState(new BlockPos(pos.getX(), y + 1, pos.getZ()));

            if (!check.isOf((Block) (Object) this)) {
                break;
            } else {
                y++;
                top = check;
            }
        }

        BlockPos placePos = new BlockPos(pos.getX(), y + 1, pos.getZ());

        if (top.canPlaceAt(world, placePos)) {
            world.setBlockState(placePos, top);
            world.setBlockState(new BlockPos(pos.getX(), y, pos.getZ()), top.with(AGE, 0), Block.NO_REDRAW);
        }
    }
}
