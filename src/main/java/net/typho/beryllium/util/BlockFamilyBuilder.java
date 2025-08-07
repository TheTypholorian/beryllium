package net.typho.beryllium.util;

import net.minecraft.block.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockFamilyBuilder {
    public final Constructor constructor;
    public final String prefix;
    public final AbstractBlock.Settings settings;
    protected Block base;
    protected final Map<BlockFamily.Variant, Block> variants = new LinkedHashMap<>();

    public BlockFamilyBuilder(Constructor constructor, String prefix, AbstractBlock.Settings settings) {
        this.constructor = constructor;
        this.prefix = prefix;
        this.settings = settings;
    }

    public BlockFamilyBuilder variant(BlockFamily.Variant variant, Block block) {
        variants.put(variant, block);
        return this;
    }

    public BlockFamily build() {
        if (base == null) {
            throw new IllegalStateException("Block family builder doesn't have base block");
        }

        BlockFamily family = new BlockFamily.Builder(base).build();
        family.getVariants().putAll(variants);
        return family;
    }

    public BlockFamilyBuilder base() {
        return base(prefix);
    }

    public BlockFamilyBuilder base(String id) {
        return base(id, new Block(settings));
    }

    public BlockFamilyBuilder base(String id, Block block) {
        base = constructor.blockWithItem(id, block, new Item.Settings());
        return this;
    }

    public BlockFamilyBuilder button(BlockSetType type, int pressTicks) {
        return button(prefix + "_button", type, pressTicks);
    }

    public BlockFamilyBuilder button(String id, BlockSetType type, int pressTicks) {
        return button(id, new ButtonBlock(type, pressTicks, settings));
    }

    public BlockFamilyBuilder button(String id, Block block) {
        return variant(BlockFamily.Variant.BUTTON, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder chiseled() {
        return chiseled("chiseled_" + prefix);
    }

    public BlockFamilyBuilder chiseled(String id) {
        return chiseled(id, new Block(settings));
    }

    public BlockFamilyBuilder chiseled(String id, Block block) {
        return variant(BlockFamily.Variant.CHISELED, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder cracked() {
        return cracked("cracked_" + prefix);
    }

    public BlockFamilyBuilder cracked(String id) {
        return cracked(id, new Block(settings));
    }

    public BlockFamilyBuilder cracked(String id, Block block) {
        return variant(BlockFamily.Variant.CRACKED, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder cut() {
        return cut("cut_" + prefix);
    }

    public BlockFamilyBuilder cut(String id) {
        return cut(id, new Block(settings));
    }

    public BlockFamilyBuilder cut(String id, Block block) {
        return variant(BlockFamily.Variant.CUT, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder door(BlockSetType type) {
        return door(prefix + "_door", type);
    }

    public BlockFamilyBuilder door(String id, BlockSetType type) {
        return door(id, new DoorBlock(type, settings));
    }

    public BlockFamilyBuilder door(String id, Block block) {
        return variant(BlockFamily.Variant.DOOR, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder customFence() {
        return customFence(prefix + "_fence");
    }

    public BlockFamilyBuilder customFence(String id) {
        return customFence(id, new FenceBlock(settings));
    }

    public BlockFamilyBuilder customFence(String id, Block block) {
        return variant(BlockFamily.Variant.CUSTOM_FENCE, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder fence() {
        return fence(prefix + "_fence");
    }

    public BlockFamilyBuilder fence(String id) {
        return fence(id, new FenceBlock(settings));
    }

    public BlockFamilyBuilder fence(String id, Block block) {
        return variant(BlockFamily.Variant.FENCE, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder mosaic() {
        return mosaic(prefix + "_mosaic");
    }

    public BlockFamilyBuilder mosaic(String id) {
        return mosaic(id, new Block(settings));
    }

    public BlockFamilyBuilder mosaic(String id, Block block) {
        return variant(BlockFamily.Variant.MOSAIC, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder sign(WoodType type) {
        return sign(prefix + "_sign", type);
    }

    public BlockFamilyBuilder sign(String id, WoodType type) {
        return sign(id, new SignBlock(type, settings));
    }

    public BlockFamilyBuilder sign(String id, Block block) {
        return variant(BlockFamily.Variant.SIGN, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder slab() {
        return slab(prefix + "_slab");
    }

    public BlockFamilyBuilder slab(String id) {
        return slab(id, new SlabBlock(settings));
    }

    public BlockFamilyBuilder slab(String id, Block block) {
        return variant(BlockFamily.Variant.SLAB, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder stairs() {
        return stairs(prefix + "_stairs");
    }

    public BlockFamilyBuilder stairs(String id) {
        return stairs(id, new StairsBlock(base.getDefaultState(), settings));
    }

    public BlockFamilyBuilder stairs(String id, Block block) {
        return variant(BlockFamily.Variant.STAIRS, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder pressurePlate(BlockSetType type) {
        return pressurePlate(prefix + "_pressure_plate", type);
    }

    public BlockFamilyBuilder pressurePlate(String id, BlockSetType type) {
        return pressurePlate(id, new PressurePlateBlock(type, settings));
    }

    public BlockFamilyBuilder pressurePlate(String id, Block block) {
        return variant(BlockFamily.Variant.PRESSURE_PLATE, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder polished() {
        return polished("polished_" + prefix);
    }

    public BlockFamilyBuilder polished(String id) {
        return polished(id, new Block(settings));
    }

    public BlockFamilyBuilder polished(String id, Block block) {
        return variant(BlockFamily.Variant.POLISHED, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder trapdoor(BlockSetType type) {
        return trapdoor(prefix + "_trapdoor", type);
    }

    public BlockFamilyBuilder trapdoor(String id, BlockSetType type) {
        return trapdoor(id, new TrapdoorBlock(type, settings));
    }

    public BlockFamilyBuilder trapdoor(String id, Block block) {
        return variant(BlockFamily.Variant.TRAPDOOR, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder wall() {
        return wall(prefix + "_wall");
    }

    public BlockFamilyBuilder wall(String id) {
        return wall(id, new WallBlock(settings));
    }

    public BlockFamilyBuilder wall(String id, Block block) {
        return variant(BlockFamily.Variant.WALL, constructor.blockWithItem(id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder wallSign(WoodType type) {
        return wallSign(prefix + "_wall", type);
    }

    public BlockFamilyBuilder wallSign(String id, WoodType type) {
        return wallSign(id, new WallSignBlock(type, settings));
    }

    public BlockFamilyBuilder wallSign(String id, Block block) {
        return variant(BlockFamily.Variant.WALL_SIGN, constructor.blockWithItem(id, block, new Item.Settings()));
    }
}
