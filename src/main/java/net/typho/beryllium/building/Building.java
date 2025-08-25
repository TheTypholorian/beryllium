package net.typho.beryllium.building;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.building.kiln.*;
import net.typho.beryllium.config.ServerConfig;
import net.typho.beryllium.util.BlockFamilyBuilder;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Building implements ModInitializer, ClientModInitializer {
    //public static final TagKey<Item> STONECUTTING_STONE = TagKey.of(RegistryKeys.ITEM, Beryllium.CONSTRUCTOR.id("stonecutting/stone"));
    //public static final TagKey<Item> STONECUTTING_STONE_BRICKS = TagKey.of(RegistryKeys.ITEM, Beryllium.CONSTRUCTOR.id("stonecutting/stone_bricks"));
    //public static final TagKey<Item> STONECUTTING_SMOOTH_STONE = TagKey.of(RegistryKeys.ITEM, Beryllium.CONSTRUCTOR.id("stonecutting/smooth_stone"));

    public static final RecipeType<KilnRecipe> KILN_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, Beryllium.BUILDING_CONSTRUCTOR.id("firing"), new RecipeType<>() {
        public String toString() {
            return "kiln";
        }
    });
    public static final RecipeSerializer<AbstractCookingRecipe> KILN_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Beryllium.BUILDING_CONSTRUCTOR.id("kiln"), new CookingRecipeSerializer<>(KilnRecipe::new, 100));
    public static final Identifier KILN_INTERACT_STAT = Beryllium.BUILDING_CONSTRUCTOR.stat("interact_with_kiln", StatFormatter.DEFAULT);
    public static final ScreenHandlerType<KilnScreenHandler> KILN_SCREEN_HANDLER_TYPE = Beryllium.BUILDING_CONSTRUCTOR.screenHandler("kiln", KilnScreenHandler::new);
    public static final Block KILN_BLOCK = Beryllium.BUILDING_CONSTRUCTOR.blockWithItem("kiln", new KilnBlock(AbstractBlock.Settings.copy(Blocks.BLAST_FURNACE)), new Item.Settings());
    public static final BlockEntityType<KilnEntity> KILN_BLOCK_ENTITY_TYPE = Beryllium.BUILDING_CONSTRUCTOR.blockEntity("kiln", BlockEntityType.Builder.create(KilnEntity::new, KILN_BLOCK));

    public static final BlockFamily MOSSY_STONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("mossy_stone", Blocks.STONE)
            .base()
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build();
    public static final BlockFamily SNOW_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("snow_brick", Blocks.SNOW_BLOCK)
            .base("snow_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.SHOVEL_MINEABLE)
            .build();
    public static final BlockFamily STONE_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("stone_brick", Blocks.STONE_BRICKS)
            .base(Blocks.STONE_BRICKS)
            .wall(Blocks.STONE_BRICK_WALL)
            .stairs(Blocks.STONE_BRICK_STAIRS)
            .slab(Blocks.STONE_BRICK_SLAB)
            .noDatagen()
            .build(BlockFamilies.STONE_BRICK);
    public static final BlockFamily CRACKED_STONE_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_stone_brick", Blocks.CRACKED_STONE_BRICKS)
            .base(Blocks.CRACKED_STONE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.STONE_BRICK)
            .build();
    public static final BlockFamily SMOOTH_STONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("smooth_stone", Blocks.SMOOTH_STONE)
            .base(Blocks.SMOOTH_STONE)
            .chiseled()
            .wall()
            .stairs()
            .slab(Blocks.SMOOTH_STONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.STONE)
            .build();
    public static final BlockFamily GRANITE_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("granite_brick", Blocks.STONE_BRICKS)
            .features(ServerConfig.graniteBricks.flag)
            .base("granite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.GRANITE, Blocks.POLISHED_GRANITE)
            .build();
    public static final BlockFamily DIORITE_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("diorite_brick", Blocks.STONE_BRICKS)
            .base("diorite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.DIORITE, Blocks.POLISHED_DIORITE)
            .build();
    public static final BlockFamily ANDESITE_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("andesite_brick", Blocks.STONE_BRICKS)
            .base("andesite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.ANDESITE, Blocks.POLISHED_ANDESITE)
            .build();
    public static final BlockFamily POLISHED_GRANITE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("polished_granite", Blocks.POLISHED_GRANITE)
            .base(Blocks.POLISHED_GRANITE)
            .wall()
            .stairs(Blocks.POLISHED_GRANITE_STAIRS)
            .slab(Blocks.POLISHED_GRANITE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.GRANITE)
            .build(BlockFamilies.POLISHED_GRANITE);
    public static final BlockFamily POLISHED_DIORITE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("polished_diorite", Blocks.POLISHED_DIORITE)
            .base(Blocks.POLISHED_DIORITE)
            .wall()
            .stairs(Blocks.POLISHED_DIORITE_STAIRS)
            .slab(Blocks.POLISHED_DIORITE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.DIORITE)
            .build(BlockFamilies.POLISHED_DIORITE);
    public static final BlockFamily POLISHED_ANDESITE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("polished_andesite", Blocks.POLISHED_ANDESITE)
            .base(Blocks.POLISHED_ANDESITE)
            .wall()
            .stairs(Blocks.POLISHED_ANDESITE_STAIRS)
            .slab(Blocks.POLISHED_ANDESITE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.ANDESITE)
            .build(BlockFamilies.POLISHED_ANDESITE);
    public static final BlockFamily CRACKED_DEEPSLATE_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_deepslate_brick", Blocks.CRACKED_DEEPSLATE_BRICKS)
            .base(Blocks.CRACKED_DEEPSLATE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.DEEPSLATE_BRICK)
            .build();
    public static final BlockFamily CRACKED_DEEPSLATE_TILES = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_deepslate_tile", Blocks.CRACKED_DEEPSLATE_TILES)
            .base(Blocks.CRACKED_DEEPSLATE_TILES)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.DEEPSLATE_TILE)
            .build();
    public static final BlockFamily CRACKED_BRICKS = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_brick", Blocks.BRICKS)
            .base("cracked_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.BRICK)
            .build();
    public static final BlockFamily PACKED_MUD = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("packed_mud", Blocks.PACKED_MUD)
            .base(Blocks.PACKED_MUD)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.SHOVEL_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_NETHER_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_nether_brick", Blocks.CRACKED_NETHER_BRICKS)
            .base(Blocks.CRACKED_NETHER_BRICKS)
            .wall()
            .fence()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.NETHER_BRICK)
            .build();
    public static final BlockFamily RED_NETHER_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("red_nether_brick", Blocks.RED_NETHER_BRICKS)
            .base(Blocks.RED_NETHER_BRICKS)
            .wall(Blocks.RED_NETHER_BRICK_WALL)
            .fence()
            .stairs(Blocks.RED_NETHER_BRICK_STAIRS)
            .slab(Blocks.RED_NETHER_BRICK_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.RED_NETHER_BRICK);
    public static final BlockFamily CRACKED_RED_NETHER_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_red_nether_brick", Blocks.CRACKED_NETHER_BRICKS)
            .base("cracked_red_nether_bricks")
            .wall()
            .fence()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.RED_NETHER_BRICK)
            .build();
    public static final BlockFamily STONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("stone", Blocks.STONE)
            .base(Blocks.STONE)
            .wall()
            .stairs(Blocks.STONE_STAIRS)
            .slab(Blocks.STONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.COBBLESTONE)
            .build(BlockFamilies.STONE);
    public static final BlockFamily QUARTZ = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("quartz", Blocks.QUARTZ_BLOCK)
            .base(Blocks.QUARTZ_BLOCK)
            .wall()
            .stairs(Blocks.QUARTZ_STAIRS)
            .slab(Blocks.QUARTZ_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.QUARTZ_BLOCK);
    public static final BlockFamily SMOOTH_QUARTZ = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("smooth_quartz", Blocks.SMOOTH_QUARTZ)
            .base(Blocks.SMOOTH_QUARTZ)
            .wall()
            .stairs(Blocks.SMOOTH_QUARTZ_STAIRS)
            .slab(Blocks.SMOOTH_QUARTZ_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.QUARTZ_BLOCK)
            .build(BlockFamilies.SMOOTH_QUARTZ);
    public static final BlockFamily QUARTZ_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("quartz_brick", Blocks.QUARTZ_BRICKS)
            .base(Blocks.QUARTZ_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.QUARTZ_BLOCK)
            .build();
    public static final BlockFamily CUT_SANDSTONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cut_sandstone", Blocks.CUT_SANDSTONE)
            .base(Blocks.CUT_SANDSTONE)
            .wall()
            .stairs()
            .slab(Blocks.CUT_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.SANDSTONE)
            .build(BlockFamilies.CUT_SANDSTONE);
    public static final BlockFamily SMOOTH_SANDSTONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("smooth_sandstone", Blocks.SMOOTH_SANDSTONE)
            .base(Blocks.SMOOTH_SANDSTONE)
            .wall()
            .stairs(Blocks.SMOOTH_SANDSTONE_STAIRS)
            .slab(Blocks.SMOOTH_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.SANDSTONE)
            .build(BlockFamilies.SMOOTH_SANDSTONE);
    public static final BlockFamily CUT_RED_SANDSTONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cut_red_sandstone", Blocks.CUT_RED_SANDSTONE)
            .base(Blocks.CUT_RED_SANDSTONE)
            .wall()
            .stairs()
            .slab(Blocks.CUT_RED_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.RED_SANDSTONE)
            .build(BlockFamilies.CUT_RED_SANDSTONE);
    public static final BlockFamily SMOOTH_RED_SANDSTONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("smooth_red_sandstone", Blocks.SMOOTH_RED_SANDSTONE)
            .base(Blocks.SMOOTH_RED_SANDSTONE)
            .wall()
            .stairs(Blocks.SMOOTH_RED_SANDSTONE_STAIRS)
            .slab(Blocks.SMOOTH_RED_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.RED_SANDSTONE)
            .build(BlockFamilies.SMOOTH_RED_SANDSTONE);
    public static final BlockFamily PRISMARINE_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("prismarine_brick", Blocks.PRISMARINE_BRICKS)
            .base(Blocks.PRISMARINE_BRICKS)
            .wall()
            .stairs(Blocks.PRISMARINE_BRICK_STAIRS)
            .slab(Blocks.PRISMARINE_BRICK_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.PRISMARINE_BRICK);
    public static final BlockFamily DARK_PRISMARINE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("dark_prismarine", Blocks.DARK_PRISMARINE)
            .base(Blocks.DARK_PRISMARINE)
            .wall()
            .stairs(Blocks.DARK_PRISMARINE_STAIRS)
            .slab(Blocks.DARK_PRISMARINE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.DARK_PRISMARINE);
    public static final BlockFamily GILDED_BLACKSTONE = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("gilded_blackstone", Blocks.GILDED_BLACKSTONE)
            .base(Blocks.GILDED_BLACKSTONE)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build();
    public static final BlockFamily CRACKED_POLISHED_BLACKSTONE_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_polished_blackstone_brick", Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
            .base(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.POLISHED_BLACKSTONE_BRICK)
            .build();
    public static final BlockFamily CRACKED_END_STONE_BRICK = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_end_stone_brick", Blocks.END_STONE_BRICKS)
            .base("cracked_end_stone_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.END_STONE_BRICK)
            .build();
    public static final BlockFamily PURPUR = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("purpur", Blocks.PURPUR_BLOCK)
            .base(Blocks.PURPUR_BLOCK)
            .wall()
            .stairs(Blocks.PURPUR_STAIRS)
            .slab(Blocks.PURPUR_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build();
    public static final BlockFamily CRACKED_PURPUR = Beryllium.BUILDING_CONSTRUCTOR.blockFamily("cracked_purpur", Blocks.PURPUR_BLOCK)
            .base("cracked_purpur_block")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.PURPUR)
            .build();

    public static void iterateBlockFamily(BlockFamily family, BiConsumer<BlockFamily.Variant, Block> out) {
        out.accept(null, family.getBaseBlock());
        family.getVariants().forEach(out);
    }

    public static void iterateBlockFamily(BlockFamilyBuilder family, BiConsumer<BlockFamily.Variant, Block> out) {
        family.datagen.forEach(out);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Blocks.SMOKER, KILN_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            BlockFamily.Variant[] variantOrder = {
                    BlockFamily.Variant.MOSAIC,
                    BlockFamily.Variant.CHISELED,
                    BlockFamily.Variant.CUT,
                    BlockFamily.Variant.STAIRS,
                    BlockFamily.Variant.SLAB,
                    BlockFamily.Variant.WALL,
                    BlockFamily.Variant.FENCE,
                    BlockFamily.Variant.FENCE_GATE,
                    BlockFamily.Variant.DOOR,
                    BlockFamily.Variant.TRAPDOOR,
                    BlockFamily.Variant.PRESSURE_PLATE,
                    BlockFamily.Variant.BUTTON,
                    BlockFamily.Variant.SIGN,
                    BlockFamily.Variant.WALL_SIGN
            };
            BlockFamily[][] vanillaVariants = {
                    {STONE, MOSSY_STONE, BlockFamilies.STONE_BRICK, CRACKED_STONE_BRICKS, BlockFamilies.MOSSY_STONE_BRICK, BlockFamilies.COBBLESTONE, BlockFamilies.MOSSY_COBBLESTONE, SMOOTH_STONE},
                    {BlockFamilies.DEEPSLATE_BRICK, CRACKED_DEEPSLATE_BRICKS},
                    {BlockFamilies.DEEPSLATE_TILE, CRACKED_DEEPSLATE_TILES},
                    {BlockFamilies.BRICK, CRACKED_BRICKS},
                    {PACKED_MUD, BlockFamilies.MUD_BRICK, SNOW_BRICKS},
                    {BlockFamilies.NETHER_BRICK, CRACKED_NETHER_BRICK},
                    {RED_NETHER_BRICK, CRACKED_RED_NETHER_BRICK},
                    {QUARTZ},
                    {SMOOTH_QUARTZ},
                    {QUARTZ_BRICK},
                    {BlockFamilies.SANDSTONE, CUT_SANDSTONE, SMOOTH_SANDSTONE},
                    {BlockFamilies.RED_SANDSTONE, CUT_RED_SANDSTONE, SMOOTH_RED_SANDSTONE},
                    {PRISMARINE_BRICK, DARK_PRISMARINE},
                    {BlockFamilies.GRANITE, POLISHED_GRANITE, GRANITE_BRICKS},
                    {BlockFamilies.DIORITE, POLISHED_DIORITE, DIORITE_BRICKS},
                    {BlockFamilies.ANDESITE, POLISHED_ANDESITE, ANDESITE_BRICKS},
                    {BlockFamilies.BLACKSTONE, GILDED_BLACKSTONE, BlockFamilies.POLISHED_BLACKSTONE, BlockFamilies.POLISHED_BLACKSTONE_BRICK, CRACKED_POLISHED_BLACKSTONE_BRICK},
                    {BlockFamilies.END_STONE_BRICK, CRACKED_END_STONE_BRICK},
                    {PURPUR, CRACKED_PURPUR},
            };

            for (BlockFamily[] variant : vanillaVariants) {
                Block keep = variant[0].getBaseBlock();

                Predicate<ItemStack> predicate = stack -> {
                    if (stack.getItem() instanceof BlockItem blockItem) {
                        Block block = blockItem.getBlock();

                        if (keep != block) {
                            for (BlockFamily family : variant) {
                                if (family.getBaseBlock() == block || family.getVariants().containsValue(block)) {
                                    return true;
                                }
                            }
                        }
                    }

                    return false;
                };

                entries.getDisplayStacks().removeIf(predicate);
                entries.getSearchTabStacks().removeIf(predicate);

                ItemStack last = new ItemStack(keep);

                for (BlockFamily family : variant) {
                    if (family.getBaseBlock() != keep) {
                        ItemStack stack = new ItemStack(family.getBaseBlock());
                        entries.addAfter(last, stack);
                        last = stack;
                    }

                    for (BlockFamily.Variant v : variantOrder) {
                        Block block = family.getVariant(v);

                        if (block != null) {
                            ItemStack stack = new ItemStack(block);
                            entries.addAfter(last, stack);
                            last = stack;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onInitializeClient() {
        HandledScreens.register(Building.KILN_SCREEN_HANDLER_TYPE, KilnScreen::new);
    }
}