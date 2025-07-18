package net.typho.beryllium.building;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.*;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.Module;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated
public class BlockSet {
    public enum Type {
        WOOD, ROCKS, CONCRETE, MISC;

        public final Map<Identifier, BlockSet> map = new LinkedHashMap<>();
    }

    public final Type type;
    public final Identifier id;
    public Block solid, chiseled, pillar, stairs, slab, button, pressurePlate, fence, fenceGate, sign, wallSign, wall, door, trapdoor;
    public boolean datagen = false;

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

    public boolean doDatagen(Block block) {
        return Objects.equals(Registries.BLOCK.getId(block).getNamespace(), Beryllium.MOD_ID);
    }

    public void genModels(BlockStateModelGenerator gen) {
        BlockStateModelGenerator.BlockTexturePool pool = gen.registerCubeAllModelTexturePool(solid);

        if (doDatagen(chiseled)) {
            gen.registerSimpleCubeAll(chiseled);
        }

        if (doDatagen(pillar)) {
            gen.registerSimpleCubeAll(pillar);
        }

        if (doDatagen(stairs)) {
            pool.stairs(stairs);
        }

        if (doDatagen(slab)) {
            pool.slab(slab);
        }

        if (doDatagen(button)) {
            pool.button(button);
        }

        if (doDatagen(pressurePlate)) {
            pool.pressurePlate(pressurePlate);
        }

        if (doDatagen(fence)) {
            pool.fence(fence);
        }

        if (doDatagen(fenceGate)) {
            pool.fenceGate(fenceGate);
        }

        if (doDatagen(sign)) {
            pool.sign(sign);
        }

        if (doDatagen(wallSign)) {
            pool.sign(wallSign);
        }
    }

    public void genRecipes(RecipeExporter exporter) {
    }

    public void genLootTables(FabricBlockLootTableProvider tables) {
    }

    public void genBlockTags(RegistryWrapper.WrapperLookup lookup, FabricTagProvider.BlockTagProvider provider) {
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

    public BlockSet genSolid(Block parent) {
        datagen = true;
        return setSolid(Module.block(id.getPath(), new Block(AbstractBlock.Settings.copy(parent))));
    }

    public BlockSet setChiseled(Block chiseled) {
        this.chiseled = chiseled;
        return this;
    }

    public BlockSet genChiseled() {
        datagen = true;
        return setChiseled(Module.block("chiseled_" + id.getPath(), new Block(AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setPillar(Block pillar) {
        this.pillar = pillar;
        return this;
    }

    public BlockSet genPillar() {
        datagen = true;
        return setPillar(Module.block(id.getPath() + "_pillar", new Block(AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setStairs(Block stairs) {
        this.stairs = stairs;
        return this;
    }

    public BlockSet genStairs() {
        datagen = true;
        return setStairs(Module.block(id.getPath() + "_stairs", new StairsBlock(solid.getDefaultState(), AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setSlab(Block slab) {
        this.slab = slab;
        return this;
    }

    public BlockSet genSlab() {
        datagen = true;
        return setSlab(Module.block(id.getPath() + "_slab", new SlabBlock(AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setButton(Block button) {
        this.button = button;
        return this;
    }

    public BlockSet genButton(BlockSetType set, int pressTicks) {
        datagen = true;
        return setButton(Module.block(id.getPath() + "_button", new ButtonBlock(set, pressTicks, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setPressurePlate(Block pressurePlate) {
        this.pressurePlate = pressurePlate;
        return this;
    }

    public BlockSet genPressurePlate(BlockSetType set) {
        datagen = true;
        return setPressurePlate(Module.block(id.getPath() + "_pressure_plate", new PressurePlateBlock(set, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setFence(Block fence) {
        this.fence = fence;
        return this;
    }

    public BlockSet genFence() {
        datagen = true;
        return setFence(Module.block(id.getPath() + "_fence", new FenceBlock(AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setFenceGate(Block fenceGate) {
        this.fenceGate = fenceGate;
        return this;
    }

    public BlockSet genFenceGate(WoodType type) {
        datagen = true;
        return setFenceGate(Module.block(id.getPath() + "_fence_gate", new FenceGateBlock(type, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setSign(Block sign) {
        this.sign = sign;
        return this;
    }

    public BlockSet genSign(WoodType type) {
        datagen = true;
        return setSign(Module.block(id.getPath() + "_sign", new SignBlock(type, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setWallSign(Block wallSign) {
        this.wallSign = wallSign;
        return this;
    }

    public BlockSet genWallSign(WoodType type) {
        datagen = true;
        return setWallSign(Module.block(id.getPath() + "_wall_sign", new WallSignBlock(type, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setWall(Block wall) {
        this.wall = wall;
        return this;
    }

    public BlockSet genWall() {
        datagen = true;
        return setWall(Module.block(id.getPath() + "_wall", new WallBlock(AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setDoor(Block door) {
        this.door = door;
        return this;
    }

    public BlockSet genDoor(BlockSetType type) {
        datagen = true;
        return setDoor(Module.block(id.getPath() + "_door", new DoorBlock(type, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setTrapdoor(Block trapdoor) {
        this.trapdoor = trapdoor;
        return this;
    }

    public BlockSet genTrapdoor(BlockSetType type) {
        datagen = true;
        return setTrapdoor(Module.block(id.getPath() + "_trapdoor", new TrapdoorBlock(type, AbstractBlock.Settings.copy(solid))));
    }

    public BlockSet setDatagen(boolean datagen) {
        this.datagen = datagen;
        return this;
    }
}
