package net.typho.beryllium.util;

import net.minecraft.block.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.SignItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureFlag;

import java.util.*;

public class BlockFamilyBuilder {
    public static final List<BlockFamilyBuilder> FAMILIES = new LinkedList<>();

    public final Constructor constructor;
    public final String prefix;
    public final AbstractBlock.Settings settings;
    public Block base;
    public final Map<BlockFamily.Variant, Block> variants = new LinkedHashMap<>();
    public final Map<BlockFamily.Variant, Block> datagen = new LinkedHashMap<>();
    public final List<TagKey<Block>> tags = new LinkedList<>();
    public final List<Ingredient> stonecutting = new LinkedList<>();
    public final List<BlockFamily> smelting = new LinkedList<>();
    public BlockFamily built;
    public SignItem signItem;

    public BlockFamilyBuilder(Constructor constructor, String prefix, AbstractBlock.Settings settings) {
        this.constructor = constructor;
        this.prefix = prefix;
        this.settings = settings;
        FAMILIES.add(this);
    }

    public BlockFamilyBuilder variant(BlockFamily.Variant variant, Block block) {
        variants.put(variant, block);
        return this;
    }

    public BlockFamily build() {
        if (built != null) {
            return built;
        }

        if (base == null) {
            throw new IllegalStateException("Block family builder missing base block");
        }

        built = new BlockFamily.Builder(base).build();
        built.getVariants().putAll(variants);
        return built;
    }

    public BlockFamily build(BlockFamily family) {
        if (built != null) {
            return built;
        }

        built = family;
        family.getVariants().putAll(variants);
        variants.putAll(family.getVariants());
        return family;
    }

    public Block register(BlockFamily.Variant variant, String id, Block block, Item.Settings settings) {
        constructor.blockWithItem(id, block, settings);
        datagen.put(variant, block);
        return block;
    }

    public Block register(BlockFamily.Variant variant, String id, Block block) {
        constructor.block(id, block);
        datagen.put(variant, block);
        return block;
    }

    public BlockFamilyBuilder stonecutting(TagKey<Item> tag) {
        stonecutting(Ingredient.fromTag(tag));
        return this;
    }

    public BlockFamilyBuilder stonecutting(Ingredient... inputs) {
        stonecutting();
        Collections.addAll(stonecutting, inputs);
        return this;
    }

    public BlockFamilyBuilder stonecutting(ItemConvertible... inputs) {
        stonecutting();
        Arrays.stream(inputs).map(Ingredient::ofItems).forEach(stonecutting::add);
        return this;
    }

    public BlockFamilyBuilder features(FeatureFlag... flags) {
        settings.requires(flags);
        return this;
    }

    public BlockFamilyBuilder stonecutting() {
        stonecutting.add(Ingredient.ofItems(base));
        return this;
    }

    public BlockFamilyBuilder smelting(BlockFamily... families) {
        Collections.addAll(smelting, families);
        return this;
    }

    public BlockFamilyBuilder noBaseDatagen() {
        return noDatagen(null);
    }

    public BlockFamilyBuilder noDatagen(BlockFamily.Variant variant) {
        datagen.remove(variant);
        return this;
    }

    public BlockFamilyBuilder noDatagen() {
        datagen.clear();
        return this;
    }

    @SafeVarargs
    public final BlockFamilyBuilder tags(TagKey<Block>... tags) {
        this.tags.addAll(Arrays.asList(tags));

        return this;
    }

    public BlockFamilyBuilder base() {
        return base(prefix);
    }

    public BlockFamilyBuilder base(String id) {
        return base(id, new Block(settings));
    }

    public BlockFamilyBuilder base(String id, Block block) {
        return base(register(null, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder base(Block block) {
        base = block;
        return this;
    }

    public BlockFamilyBuilder button(BlockSetType type, int pressTicks) {
        return button(prefix + "_button", type, pressTicks);
    }

    public BlockFamilyBuilder button(String id, BlockSetType type, int pressTicks) {
        return button(id, new ButtonBlock(type, pressTicks, settings));
    }

    public BlockFamilyBuilder button(String id, Block block) {
        return button(register(BlockFamily.Variant.BUTTON, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder button(Block block) {
        return variant(BlockFamily.Variant.BUTTON, block);
    }

    public BlockFamilyBuilder chiseled() {
        return chiseled("chiseled_" + prefix);
    }

    public BlockFamilyBuilder chiseled(String id) {
        return chiseled(id, new Block(settings));
    }

    public BlockFamilyBuilder chiseled(String id, Block block) {
        return chiseled(register(BlockFamily.Variant.CHISELED, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder chiseled(Block block) {
        return variant(BlockFamily.Variant.CHISELED, block);
    }

    public BlockFamilyBuilder cracked() {
        return cracked("cracked_" + prefix);
    }

    public BlockFamilyBuilder cracked(String id) {
        return cracked(id, new Block(settings));
    }

    public BlockFamilyBuilder cracked(String id, Block block) {
        return cracked(register(BlockFamily.Variant.CRACKED, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder cracked(Block block) {
        return variant(BlockFamily.Variant.CRACKED, block);
    }

    public BlockFamilyBuilder cut() {
        return cut("cut_" + prefix);
    }

    public BlockFamilyBuilder cut(String id) {
        return cut(id, new Block(settings));
    }

    public BlockFamilyBuilder cut(String id, Block block) {
        return cut(register(BlockFamily.Variant.CUT, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder cut(Block block) {
        return variant(BlockFamily.Variant.CUT, block);
    }

    public BlockFamilyBuilder door(BlockSetType type) {
        return door(prefix + "_door", type);
    }

    public BlockFamilyBuilder door(String id, BlockSetType type) {
        return door(id, new DoorBlock(type, settings));
    }

    public BlockFamilyBuilder door(String id, Block block) {
        return door(register(BlockFamily.Variant.DOOR, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder door(Block block) {
        return variant(BlockFamily.Variant.DOOR, block);
    }

    public BlockFamilyBuilder customFence() {
        return customFence(prefix + "_fence");
    }

    public BlockFamilyBuilder customFence(String id) {
        return customFence(id, new FenceBlock(settings));
    }

    public BlockFamilyBuilder customFence(String id, Block block) {
        return customFence(register(BlockFamily.Variant.CUSTOM_FENCE, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder customFence(Block block) {
        return variant(BlockFamily.Variant.CUSTOM_FENCE, block);
    }

    public BlockFamilyBuilder fence() {
        return fence(prefix + "_fence");
    }

    public BlockFamilyBuilder fence(String id) {
        return fence(id, new FenceBlock(settings));
    }

    public BlockFamilyBuilder fence(String id, Block block) {
        return fence(register(BlockFamily.Variant.FENCE, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder fence(Block block) {
        return variant(BlockFamily.Variant.FENCE, block);
    }

    public BlockFamilyBuilder customFenceGate(WoodType type) {
        return customFenceGate(prefix + "_fence_gate", type);
    }

    public BlockFamilyBuilder customFenceGate(String id, WoodType type) {
        return customFenceGate(id, new FenceGateBlock(type, settings));
    }

    public BlockFamilyBuilder customFenceGate(String id, Block block) {
        return customFenceGate(register(BlockFamily.Variant.CUSTOM_FENCE_GATE, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder customFenceGate(Block block) {
        return variant(BlockFamily.Variant.CUSTOM_FENCE_GATE, block);
    }

    public BlockFamilyBuilder fenceGate(WoodType type) {
        return fenceGate(prefix + "_fence_gate", type);
    }

    public BlockFamilyBuilder fenceGate(String id, WoodType type) {
        return fenceGate(id, new FenceGateBlock(type, settings));
    }

    public BlockFamilyBuilder fenceGate(String id, Block block) {
        return fenceGate(register(BlockFamily.Variant.FENCE_GATE, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder fenceGate(Block block) {
        return variant(BlockFamily.Variant.FENCE_GATE, block);
    }

    public BlockFamilyBuilder mosaic() {
        return mosaic(prefix + "_mosaic");
    }

    public BlockFamilyBuilder mosaic(String id) {
        return mosaic(id, new Block(settings));
    }

    public BlockFamilyBuilder mosaic(String id, Block block) {
        return mosaic(register(BlockFamily.Variant.MOSAIC, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder mosaic(Block block) {
        return variant(BlockFamily.Variant.MOSAIC, block);
    }

    public BlockFamilyBuilder sign(WoodType type) {
        return sign(prefix + "_sign", type);
    }

    public BlockFamilyBuilder sign(String id, WoodType type) {
        return sign(id, new SignBlock(type, settings));
    }

    public BlockFamilyBuilder sign(String id, Block block) {
        return sign(register(BlockFamily.Variant.SIGN, id, block));
    }

    public BlockFamilyBuilder sign(Block block) {
        return variant(BlockFamily.Variant.SIGN, block);
    }

    public BlockFamilyBuilder slab() {
        return slab(prefix + "_slab");
    }

    public BlockFamilyBuilder slab(String id) {
        return slab(id, new SlabBlock(settings));
    }

    public BlockFamilyBuilder slab(String id, Block block) {
        return slab(register(BlockFamily.Variant.SLAB, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder slab(Block block) {
        return variant(BlockFamily.Variant.SLAB, block);
    }

    public BlockFamilyBuilder stairs() {
        return stairs(prefix + "_stairs");
    }

    public BlockFamilyBuilder stairs(String id) {
        return stairs(id, new StairsBlock(base.getDefaultState(), settings));
    }

    public BlockFamilyBuilder stairs(String id, Block block) {
        return stairs(register(BlockFamily.Variant.STAIRS, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder stairs(Block block) {
        return variant(BlockFamily.Variant.STAIRS, block);
    }

    public BlockFamilyBuilder pressurePlate(BlockSetType type) {
        return pressurePlate(prefix + "_pressure_plate", type);
    }

    public BlockFamilyBuilder pressurePlate(String id, BlockSetType type) {
        return pressurePlate(id, new PressurePlateBlock(type, settings));
    }

    public BlockFamilyBuilder pressurePlate(String id, Block block) {
        return pressurePlate(register(BlockFamily.Variant.PRESSURE_PLATE, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder pressurePlate(Block block) {
        return variant(BlockFamily.Variant.PRESSURE_PLATE, block);
    }

    public BlockFamilyBuilder polished() {
        return polished("polished_" + prefix);
    }

    public BlockFamilyBuilder polished(String id) {
        return polished(id, new Block(settings));
    }

    public BlockFamilyBuilder polished(String id, Block block) {
        return polished(register(BlockFamily.Variant.POLISHED, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder polished(Block block) {
        return variant(BlockFamily.Variant.POLISHED, block);
    }

    public BlockFamilyBuilder trapdoor(BlockSetType type) {
        return trapdoor(prefix + "_trapdoor", type);
    }

    public BlockFamilyBuilder trapdoor(String id, BlockSetType type) {
        return trapdoor(id, new TrapdoorBlock(type, settings));
    }

    public BlockFamilyBuilder trapdoor(String id, Block block) {
        return trapdoor(register(BlockFamily.Variant.TRAPDOOR, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder trapdoor(Block block) {
        return variant(BlockFamily.Variant.TRAPDOOR, block);
    }

    public BlockFamilyBuilder wall() {
        return wall(prefix + "_wall");
    }

    public BlockFamilyBuilder wall(String id) {
        return wall(id, new WallBlock(settings));
    }

    public BlockFamilyBuilder wall(String id, Block block) {
        return wall(register(BlockFamily.Variant.WALL, id, block, new Item.Settings()));
    }

    public BlockFamilyBuilder wall(Block block) {
        return variant(BlockFamily.Variant.WALL, block);
    }

    public BlockFamilyBuilder wallSign(WoodType type) {
        return wallSign(prefix + "_wall_sign", type);
    }

    public BlockFamilyBuilder wallSign(String id, WoodType type) {
        Block sign = variants.get(BlockFamily.Variant.SIGN);
        return wallSign(id, new WallSignBlock(type, AbstractBlock.Settings.copy(sign).dropsLike(sign)));
    }

    public BlockFamilyBuilder wallSign(String id, Block block) {
        return wallSign(register(BlockFamily.Variant.WALL_SIGN, id, block));
    }

    public BlockFamilyBuilder wallSign(Block block) {
        return variant(BlockFamily.Variant.WALL_SIGN, block);
    }

    public BlockFamilyBuilder signItem() {
        Block sign = variants.get(BlockFamily.Variant.SIGN);
        Block wallSign = variants.get(BlockFamily.Variant.WALL_SIGN);

        signItem = constructor.item(prefix + "_sign", new SignItem(new Item.Settings().maxCount(16), sign, wallSign));

        return this;
    }
}
