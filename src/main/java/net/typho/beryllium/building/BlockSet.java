package net.typho.beryllium.building;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockSet {
    public enum Type {
        WOOD, ROCKS, CONCRETE, MISC;

        public final Map<Identifier, BlockSet> map = new LinkedHashMap<>();
    }

    public final Type type;
    public final Identifier id;
    public Block solid, chiseled, pillar, stairs, slab, button, pressurePlate, fence, fenceGate, sign, wallSign, wall, door, trapdoor;

    public BlockSet(Type type, Identifier id) {
        this.type = type;
        this.id = id;
        type.map.put(id, this);
    }

    public Block[] toArray() {
        return new Block[]{
                solid,
                chiseled,
                pillar,
                stairs,
                slab,
                button,
                pressurePlate,
                fence,
                fenceGate,
                sign,
                wallSign,
                wall,
                door,
                trapdoor
        };
    }

    protected boolean place(World world, BlockPos.Mutable pos, Block block) {
        if (block != null) {
            if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
                return false;
            }

            world.setBlockState(pos, block.getDefaultState());
        }

        pos.setX(pos.getX() + 1);

        return true;
    }

    public boolean place(World world, BlockPos.Mutable pos) {
        for (Block block : toArray()) {
            if (!place(world, pos, block)) {
                return false;
            }
        }

        return true;
    }

    public BlockSet setSolid(Block solid) {
        this.solid = solid;
        return this;
    }

    public BlockSet setChiseled(Block chiseled) {
        this.chiseled = chiseled;
        return this;
    }

    public BlockSet setPillar(Block pillar) {
        this.pillar = pillar;
        return this;
    }

    public BlockSet setStairs(Block stairs) {
        this.stairs = stairs;
        return this;
    }

    public BlockSet setSlab(Block slab) {
        this.slab = slab;
        return this;
    }

    public BlockSet setButton(Block button) {
        this.button = button;
        return this;
    }

    public BlockSet setPressurePlate(Block pressurePlate) {
        this.pressurePlate = pressurePlate;
        return this;
    }

    public BlockSet setFence(Block fence) {
        this.fence = fence;
        return this;
    }

    public BlockSet setFenceGate(Block fenceGate) {
        this.fenceGate = fenceGate;
        return this;
    }

    public BlockSet setSign(Block sign) {
        this.sign = sign;
        return this;
    }

    public BlockSet setWallSign(Block wallSign) {
        this.wallSign = wallSign;
        return this;
    }

    public BlockSet setWall(Block wall) {
        this.wall = wall;
        return this;
    }

    public BlockSet setDoor(Block door) {
        this.door = door;
        return this;
    }

    public BlockSet setTrapdoor(Block trapdoor) {
        this.trapdoor = trapdoor;
        return this;
    }
}
