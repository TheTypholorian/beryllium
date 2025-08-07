package net.typho.beryllium.building;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentType;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.typho.beryllium.building.kiln.*;
import net.typho.beryllium.util.Constructor;

import java.util.Objects;

public class Building implements ModInitializer, ClientModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("building");

    public static final RecipeType<KilnRecipe> KILN_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, CONSTRUCTOR.id("firing"), new RecipeType<>() {
        public String toString() {
            return "kiln";
        }
    });
    public static final RecipeSerializer<AbstractCookingRecipe> KILN_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, CONSTRUCTOR.id("kiln"), new CookingRecipeSerializer<>(KilnRecipe::new, 100));
    public static final Identifier KILN_INTERACT_STAT = CONSTRUCTOR.stat("interact_with_kiln", StatFormatter.DEFAULT);
    public static final ScreenHandlerType<KilnScreenHandler> KILN_SCREEN_HANDLER_TYPE = CONSTRUCTOR.screenHandler("kiln", KilnScreenHandler::new);
    public static final Block KILN_BLOCK = CONSTRUCTOR.blockWithItem("kiln", new KilnBlock(AbstractBlock.Settings.copy(Blocks.BLAST_FURNACE)), new Item.Settings());
    public static final BlockEntityType<KilnEntity> KILN_BLOCK_ENTITY_TYPE = CONSTRUCTOR.blockEntity("kiln", BlockEntityType.Builder.create(KilnEntity::new, KILN_BLOCK));

    public static final ComponentType<BlockPos> FILLING_WAND_COMPONENT_TYPE = CONSTRUCTOR.dataComponent("filling_wand_component", builder -> builder.codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC));
    public static final Item FILLING_WAND_ITEM = CONSTRUCTOR.item("filling_wand", new FillingWandItem(new Item.Settings()));

    public static final BlockFamily MOSSY_STONE = new BlockFamily.Builder(CONSTRUCTOR.blockWithItem("mossy_stone", new Block(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .wall(CONSTRUCTOR.blockWithItem("mossy_stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .stairs(CONSTRUCTOR.blockWithItem("mossy_stone_stairs", new StairsBlock(Blocks.STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .slab(CONSTRUCTOR.blockWithItem("mossy_stone_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings()))
            .build();
    public static final BlockFamily CRACKED_STONE_BRICKS = new BlockFamily.Builder(Blocks.CRACKED_STONE_BRICKS)
            .wall(CONSTRUCTOR.blockWithItem("cracked_stone_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)), new Item.Settings()))
            .stairs(CONSTRUCTOR.blockWithItem("cracked_stone_brick_stairs", new StairsBlock(Blocks.STONE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)), new Item.Settings()))
            .slab(CONSTRUCTOR.blockWithItem("cracked_stone_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)), new Item.Settings()))
            .build();
    public static final BlockFamily SMOOTH_STONE = new BlockFamily.Builder(Blocks.SMOOTH_STONE)
            .chiseled(CONSTRUCTOR.blockWithItem("chiseled_smooth_stone", new Block(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE)), new Item.Settings()))
            .wall(CONSTRUCTOR.blockWithItem("smooth_stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE)), new Item.Settings()))
            .stairs(CONSTRUCTOR.blockWithItem("smooth_stone_stairs", new StairsBlock(Blocks.SMOOTH_STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE)), new Item.Settings()))
            .slab(Blocks.SMOOTH_STONE_SLAB)
            .build();
    public static final BlockFamily SNOW_BRICKS = new BlockFamily.Builder(CONSTRUCTOR.blockWithItem("snow_bricks", new Block(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .chiseled(CONSTRUCTOR.blockWithItem("chiseled_snow_bricks", new Block(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .wall(CONSTRUCTOR.blockWithItem("snow_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .stairs(CONSTRUCTOR.blockWithItem("snow_brick_stairs", new StairsBlock(Blocks.SNOW_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .slab(CONSTRUCTOR.blockWithItem("snow_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.SNOW_BLOCK)), new Item.Settings()))
            .build();
    public static final BlockFamily GRANITE_BRICKS = CONSTRUCTOR.blockFamily("granite_brick", Blocks.STONE_BRICKS)
            .base("granite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily DIORITE_BRICKS = CONSTRUCTOR.blockFamily("diorite_brick", Blocks.STONE_BRICKS)
            .base("diorite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily ANDESITE_BRICKS = CONSTRUCTOR.blockFamily("andesite_brick", Blocks.STONE_BRICKS)
            .base("andesite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_DEEPSLATE_BRICKS = CONSTRUCTOR.blockFamily("cracked_deepslate_brick", Blocks.CRACKED_DEEPSLATE_BRICKS)
            .base(Blocks.CRACKED_DEEPSLATE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_DEEPSLATE_TILES = CONSTRUCTOR.blockFamily("cracked_deepslate_tile", Blocks.CRACKED_DEEPSLATE_TILES)
            .base(Blocks.CRACKED_DEEPSLATE_TILES)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_BRICKS = CONSTRUCTOR.blockFamily("cracked_brick", Blocks.BRICKS)
            .base("cracked_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily PACKED_MUD = CONSTRUCTOR.blockFamily("packed_mud", Blocks.PACKED_MUD)
            .base(Blocks.PACKED_MUD)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.SHOVEL_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_NETHER_BRICKS = CONSTRUCTOR.blockFamily("cracked_nether_brick", Blocks.CRACKED_NETHER_BRICKS)
            .base(Blocks.CRACKED_NETHER_BRICKS)
            .wall()
            .fence()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_RED_NETHER_BRICKS = CONSTRUCTOR.blockFamily("cracked_red_nether_brick", Blocks.CRACKED_NETHER_BRICKS)
            .base("cracked_red_nether_bricks")
            .wall()
            .fence()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .build();
    public static final Block STONE_WALL = CONSTRUCTOR.blockWithItem("stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE)), new Item.Settings());
    public static final Block SMOOTH_QUARTZ_WALL = CONSTRUCTOR.blockWithItem("smooth_quartz_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ)), new Item.Settings());
    public static final Block CUT_SANDSTONE_WALL = CONSTRUCTOR.blockWithItem("cut_sandstone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.CUT_SANDSTONE)), new Item.Settings());
    public static final Block CUT_SANDSTONE_STAIRS = CONSTRUCTOR.blockWithItem("cut_sandstone_stairs", new StairsBlock(Blocks.CUT_SANDSTONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.CUT_SANDSTONE)), new Item.Settings());
    public static final Block SMOOTH_SANDSTONE_WALL = CONSTRUCTOR.blockWithItem("smooth_sandstone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_SANDSTONE)), new Item.Settings());
    public static final Block CUT_RED_SANDSTONE_WALL = CONSTRUCTOR.blockWithItem("cut_red_sandstone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.CUT_RED_SANDSTONE)), new Item.Settings());
    public static final Block CUT_RED_SANDSTONE_STAIRS = CONSTRUCTOR.blockWithItem("cut_red_sandstone_stairs", new StairsBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.CUT_RED_SANDSTONE)), new Item.Settings());
    public static final Block SMOOTH_RED_SANDSTONE_WALL = CONSTRUCTOR.blockWithItem("smooth_red_sandstone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_RED_SANDSTONE)), new Item.Settings());
    public static final Block PRISMARINE_BRICK_WALL = CONSTRUCTOR.blockWithItem("prismarine_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.PRISMARINE_BRICKS)), new Item.Settings());
    public static final Block DARK_PRISMARINE_WALL = CONSTRUCTOR.blockWithItem("dark_prismarine_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.DARK_PRISMARINE)), new Item.Settings());

    @Override
    public void onInitialize() {
        BlockFamilies.STONE.getVariants().put(BlockFamily.Variant.WALL, STONE_WALL);
        BlockFamilies.SMOOTH_QUARTZ.getVariants().put(BlockFamily.Variant.WALL, SMOOTH_QUARTZ_WALL);
        BlockFamilies.CUT_SANDSTONE.getVariants().put(BlockFamily.Variant.STAIRS, CUT_SANDSTONE_STAIRS);
        BlockFamilies.CUT_SANDSTONE.getVariants().put(BlockFamily.Variant.WALL, CUT_SANDSTONE_WALL);
        BlockFamilies.SMOOTH_SANDSTONE.getVariants().put(BlockFamily.Variant.WALL, SMOOTH_SANDSTONE_WALL);
        BlockFamilies.CUT_RED_SANDSTONE.getVariants().put(BlockFamily.Variant.STAIRS, CUT_RED_SANDSTONE_STAIRS);
        BlockFamilies.CUT_RED_SANDSTONE.getVariants().put(BlockFamily.Variant.WALL, CUT_RED_SANDSTONE_WALL);
        BlockFamilies.SMOOTH_RED_SANDSTONE.getVariants().put(BlockFamily.Variant.WALL, SMOOTH_RED_SANDSTONE_WALL);
        BlockFamilies.PRISMARINE_BRICK.getVariants().put(BlockFamily.Variant.WALL, PRISMARINE_BRICK_WALL);
        BlockFamilies.DARK_PRISMARINE.getVariants().put(BlockFamily.Variant.WALL, DARK_PRISMARINE_WALL);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
                .register(entries -> {
                    entries.addAfter(
                            Items.STONE_SLAB,
                            STONE_WALL
                    );
                    entries.addAfter(
                            Items.SMOOTH_QUARTZ,
                            SMOOTH_QUARTZ_WALL
                    );
                    entries.addAfter(
                            Items.POLISHED_GRANITE,
                            GRANITE_BRICKS.getBaseBlock(),
                            GRANITE_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            GRANITE_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            GRANITE_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            Items.POLISHED_DIORITE,
                            DIORITE_BRICKS.getBaseBlock(),
                            DIORITE_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            DIORITE_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            DIORITE_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            Items.POLISHED_ANDESITE,
                            ANDESITE_BRICKS.getBaseBlock(),
                            ANDESITE_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            ANDESITE_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            ANDESITE_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.getDisplayStacks().removeIf(stack -> stack.getItem() == Items.CRACKED_DEEPSLATE_BRICKS);
                    entries.getSearchTabStacks().removeIf(stack -> stack.getItem() == Items.CRACKED_DEEPSLATE_BRICKS);
                    entries.addAfter(
                            Items.DEEPSLATE_BRICK_WALL,
                            CRACKED_DEEPSLATE_BRICKS.getBaseBlock(),
                            CRACKED_DEEPSLATE_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_DEEPSLATE_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            CRACKED_DEEPSLATE_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.getDisplayStacks().removeIf(stack -> stack.getItem() == Items.CRACKED_DEEPSLATE_TILES);
                    entries.getSearchTabStacks().removeIf(stack -> stack.getItem() == Items.CRACKED_DEEPSLATE_TILES);
                    entries.addAfter(
                            Items.DEEPSLATE_TILE_WALL,
                            CRACKED_DEEPSLATE_TILES.getBaseBlock(),
                            CRACKED_DEEPSLATE_TILES.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_DEEPSLATE_TILES.getVariant(BlockFamily.Variant.SLAB),
                            CRACKED_DEEPSLATE_TILES.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            Items.BRICK_WALL,
                            CRACKED_BRICKS.getBaseBlock(),
                            CRACKED_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            CRACKED_BRICKS.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.addAfter(
                            PACKED_MUD.getBaseBlock(),
                            PACKED_MUD.getVariant(BlockFamily.Variant.STAIRS),
                            PACKED_MUD.getVariant(BlockFamily.Variant.SLAB),
                            PACKED_MUD.getVariant(BlockFamily.Variant.WALL)
                    );
                    entries.getDisplayStacks().removeIf(stack -> stack.getItem() == Items.CRACKED_NETHER_BRICKS);
                    entries.getSearchTabStacks().removeIf(stack -> stack.getItem() == Items.CRACKED_NETHER_BRICKS);
                    entries.addAfter(
                            Items.NETHER_BRICK_FENCE,
                            CRACKED_NETHER_BRICKS.getBaseBlock(),
                            CRACKED_NETHER_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_NETHER_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            CRACKED_NETHER_BRICKS.getVariant(BlockFamily.Variant.WALL),
                            CRACKED_NETHER_BRICKS.getVariant(BlockFamily.Variant.FENCE)
                    );
                    entries.addAfter(
                            Items.RED_NETHER_BRICK_WALL,
                            CRACKED_RED_NETHER_BRICKS.getBaseBlock(),
                            CRACKED_RED_NETHER_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_RED_NETHER_BRICKS.getVariant(BlockFamily.Variant.SLAB),
                            CRACKED_RED_NETHER_BRICKS.getVariant(BlockFamily.Variant.WALL),
                            CRACKED_RED_NETHER_BRICKS.getVariant(BlockFamily.Variant.FENCE)
                    );
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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register(entries -> {
                    entries.addAfter(
                            Items.SMOKER,
                            KILN_BLOCK
                    );
                });
    }

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hit) -> {
            PlayerEntity player = MinecraftClient.getInstance().player;

            if (player != null) {
                ItemStack held = player.getMainHandStack();

                if (held.getItem() instanceof FillingWandItem && hit instanceof BlockHitResult blockHit) {
                    MatrixStack matrices = Objects.requireNonNull(context.matrixStack());
                    Vec3d cam = context.camera().getPos();

                    matrices.push();
                    matrices.translate(-cam.x, -cam.y, -cam.z);
                    RenderSystem.disableDepthTest();

                    BlockBox box = FillingWandItem.getSelection(player, held, blockHit);

                    WorldRenderer.drawBox(
                            matrices,
                            Objects.requireNonNull(context.consumers()).getBuffer(RenderLayer.getLines()),
                            box.getMinX(), box.getMinY(), box.getMinZ(),
                            box.getMaxX() + 1, box.getMaxY() + 1, box.getMaxZ() + 1,
                            1, 1, 1, 1,
                            0.5f, 0.5f, 0.5f
                    );

                    RenderSystem.enableDepthTest();
                    matrices.pop();

                    return false;
                }
            }

            return true;
        });
        HandledScreens.register(Building.KILN_SCREEN_HANDLER_TYPE, KilnScreen::new);
    }
}