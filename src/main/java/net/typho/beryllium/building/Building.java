package net.typho.beryllium.building;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.component.ComponentType;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.Module;
import net.typho.beryllium.building.kiln.*;

public class Building extends Module {
    public final RecipeType<KilnRecipe> KILN_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, id("firing"), new RecipeType<>() {
        public String toString() {
            return "kiln";
        }
    });
    public final RecipeSerializer<AbstractCookingRecipe> KILN_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, id("kiln"), new CookingRecipeSerializer<>(KilnRecipe::new, 100));
    public final Identifier KILN_INTERACT_STAT = stat("interact_with_kiln", StatFormatter.DEFAULT);
    public final ScreenHandlerType<KilnScreenHandler> KILN_SCREEN_HANDLER_TYPE = screenHandler("kiln", KilnScreenHandler::new);
    public final Block KILN_BLOCK = blockWithItem("kiln", new KilnBlock(AbstractBlock.Settings.copy(Blocks.BLAST_FURNACE)), new Item.Settings());
    public final BlockEntityType<KilnEntity> KILN_BLOCK_ENTITY_TYPE = blockEntity("kiln", BlockEntityType.Builder.create(KilnEntity::new, KILN_BLOCK));

    public final ComponentType<BlockPos> MAGIC_WAND_COMPONENT_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE, id("magic_wand_component"), ComponentType.<BlockPos>builder().codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC).build());
    public final Item MAGIC_WAND_ITEM = Registry.register(Registries.ITEM, id("magic_wand"), new MagicWandItem(new Item.Settings()));

    public final BlockFamily MOSSY_STONE = new BlockFamily.Builder(blockWithItem("mossy_stone", new Block(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .wall(blockWithItem("mossy_stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .stairs(blockWithItem("mossy_stone_stairs", new StairsBlock(Blocks.STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .slab(blockWithItem("mossy_stone_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .build();
    public final BlockFamily CRACKED_STONE_BRICKS = new BlockFamily.Builder(Blocks.CRACKED_STONE_BRICKS)
            .wall(blockWithItem("cracked_stone_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)), new Item.Settings()))
            .stairs(blockWithItem("cracked_stone_brick_stairs", new StairsBlock(Blocks.STONE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)), new Item.Settings()))
            .slab(blockWithItem("cracked_stone_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)), new Item.Settings()))
            .build();
    public final BlockFamily SMOOTH_STONE = new BlockFamily.Builder(Blocks.SMOOTH_STONE)
            .chiseled(blockWithItem("chiseled_smooth_stone", new Block(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE)), new Item.Settings()))
            .wall(blockWithItem("smooth_stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE)), new Item.Settings()))
            .stairs(blockWithItem("smooth_stone_stairs", new StairsBlock(Blocks.SMOOTH_STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE)), new Item.Settings()))
            .slab(Blocks.SMOOTH_STONE_SLAB)
            .build();
    public final BlockFamily SNOW_BRICKS = new BlockFamily.Builder(blockWithItem("snow_bricks", new Block(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .chiseled(blockWithItem("chiseled_snow_bricks", new Block(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .wall(blockWithItem("snow_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .stairs(blockWithItem("snow_brick_stairs", new StairsBlock(Blocks.SNOW_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .slab(blockWithItem("snow_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .build();

    public Building(String name) {
        super(name);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
                .register(entries -> {
                    entries.addAfter(
                            Items.STONE_BUTTON,
                            MOSSY_STONE.getBaseBlock(),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            CRACKED_STONE_BRICKS.getBaseBlock(),
                            CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            Items.SMOOTH_STONE,
                            SMOOTH_STONE.getVariant(BlockFamily.Variant.CHISELED),
                            SMOOTH_STONE.getVariant(BlockFamily.Variant.STAIRS)
                    );
                    entries.addAfter(
                            Items.SMOOTH_STONE_SLAB,
                            SMOOTH_STONE.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            Items.MUD_BRICK_WALL,
                            SNOW_BRICKS.getBaseBlock(),
                            SNOW_BRICKS.getVariant(BlockFamily.Variant.CHISELED),
                            SNOW_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            SNOW_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            SNOW_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                });
        HandledScreens.register(KILN_SCREEN_HANDLER_TYPE, KilnScreen::new);
    }
}