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

    public static final ComponentType<BlockPos> MAGIC_WAND_COMPONENT_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE, CONSTRUCTOR.id("magic_wand_component"), ComponentType.<BlockPos>builder().codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC).build());
    public static final Item FILLING_WAND_ITEM = Registry.register(Registries.ITEM, CONSTRUCTOR.id("filling_wand"), new FillingWandItem(new Item.Settings()));

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