package net.typho.beryllium.building;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.typho.beryllium.building.kiln.*;
import net.typho.beryllium.util.Constructor;

import java.util.Objects;
import java.util.function.Predicate;

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

    public static final BlockFamily MOSSY_STONE = CONSTRUCTOR.blockFamily("mossy_stone", Blocks.STONE)
            .base()
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build();
    public static final BlockFamily SNOW = CONSTRUCTOR.blockFamily("snow", Blocks.SNOW_BLOCK)
            .base(Blocks.SNOW_BLOCK)
            .tags(BlockTags.SHOVEL_MINEABLE)
            .build();
    public static final BlockFamily SNOW_BRICKS = CONSTRUCTOR.blockFamily("snow_brick", Blocks.SNOW_BLOCK)
            .base("snow_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.SHOVEL_MINEABLE)
            .build();
    public static final BlockFamily CRACKED_STONE_BRICKS = CONSTRUCTOR.blockFamily("cracked_stone_brick", Blocks.CRACKED_STONE_BRICKS)
            .base(Blocks.CRACKED_STONE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.STONE_BRICK)
            .build();
    public static final BlockFamily SMOOTH_STONE = CONSTRUCTOR.blockFamily("smooth_stone", Blocks.SMOOTH_STONE)
            .base(Blocks.SMOOTH_STONE)
            .chiseled()
            .wall()
            .stairs()
            .slab(Blocks.SMOOTH_STONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.STONE)
            .build();
    public static final BlockFamily GRANITE_BRICKS = CONSTRUCTOR.blockFamily("granite_brick", Blocks.STONE_BRICKS)
            .base("granite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.GRANITE)
            .build();
    public static final BlockFamily DIORITE_BRICKS = CONSTRUCTOR.blockFamily("diorite_brick", Blocks.STONE_BRICKS)
            .base("diorite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.DIORITE)
            .build();
    public static final BlockFamily ANDESITE_BRICKS = CONSTRUCTOR.blockFamily("andesite_brick", Blocks.STONE_BRICKS)
            .base("andesite_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.ANDESITE)
            .build();
    public static final BlockFamily CRACKED_DEEPSLATE_BRICKS = CONSTRUCTOR.blockFamily("cracked_deepslate_brick", Blocks.CRACKED_DEEPSLATE_BRICKS)
            .base(Blocks.CRACKED_DEEPSLATE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.DEEPSLATE_BRICK)
            .build();
    public static final BlockFamily CRACKED_DEEPSLATE_TILES = CONSTRUCTOR.blockFamily("cracked_deepslate_tile", Blocks.CRACKED_DEEPSLATE_TILES)
            .base(Blocks.CRACKED_DEEPSLATE_TILES)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.DEEPSLATE_TILE)
            .build();
    public static final BlockFamily CRACKED_BRICKS = CONSTRUCTOR.blockFamily("cracked_brick", Blocks.BRICKS)
            .base("cracked_bricks")
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.BRICK)
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
            .stonecutting()
            .smelting(BlockFamilies.NETHER_BRICK)
            .build();
    public static final BlockFamily CRACKED_RED_NETHER_BRICKS = CONSTRUCTOR.blockFamily("cracked_red_nether_brick", Blocks.CRACKED_NETHER_BRICKS)
            .base("cracked_red_nether_bricks")
            .wall()
            .fence()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.RED_NETHER_BRICK)
            .build();
    public static final BlockFamily STONE = CONSTRUCTOR.blockFamily("stone", Blocks.STONE)
            .base(Blocks.STONE)
            .wall()
            .stairs(Blocks.STONE_STAIRS)
            .slab(Blocks.STONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.COBBLESTONE)
            .build(BlockFamilies.STONE);
    public static final BlockFamily QUARTZ = CONSTRUCTOR.blockFamily("quartz", Blocks.QUARTZ_BLOCK)
            .base(Blocks.QUARTZ_BLOCK)
            .wall()
            .stairs(Blocks.QUARTZ_STAIRS)
            .slab(Blocks.QUARTZ_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.QUARTZ_BLOCK);
    public static final BlockFamily SMOOTH_QUARTZ = CONSTRUCTOR.blockFamily("smooth_quartz", Blocks.SMOOTH_QUARTZ)
            .base(Blocks.SMOOTH_QUARTZ)
            .wall()
            .stairs(Blocks.SMOOTH_QUARTZ_STAIRS)
            .slab(Blocks.SMOOTH_QUARTZ_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.QUARTZ_BLOCK)
            .build(BlockFamilies.SMOOTH_QUARTZ);
    public static final BlockFamily QUARTZ_BRICK = CONSTRUCTOR.blockFamily("quartz_brick", Blocks.QUARTZ_BRICKS)
            .base(Blocks.QUARTZ_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.QUARTZ_BLOCK)
            .build();
    public static final BlockFamily CUT_SANDSTONE = CONSTRUCTOR.blockFamily("cut_sandstone", Blocks.CUT_SANDSTONE)
            .base(Blocks.CUT_SANDSTONE)
            .wall()
            .stairs()
            .slab(Blocks.CUT_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.SANDSTONE)
            .build(BlockFamilies.CUT_SANDSTONE);
    public static final BlockFamily SMOOTH_SANDSTONE = CONSTRUCTOR.blockFamily("smooth_sandstone", Blocks.SMOOTH_SANDSTONE)
            .base(Blocks.SMOOTH_SANDSTONE)
            .wall()
            .stairs(Blocks.SMOOTH_SANDSTONE_STAIRS)
            .slab(Blocks.SMOOTH_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.SANDSTONE)
            .build(BlockFamilies.SMOOTH_SANDSTONE);
    public static final BlockFamily CUT_RED_SANDSTONE = CONSTRUCTOR.blockFamily("cut_red_sandstone", Blocks.CUT_RED_SANDSTONE)
            .base(Blocks.CUT_RED_SANDSTONE)
            .wall()
            .stairs()
            .slab(Blocks.CUT_RED_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.RED_SANDSTONE)
            .build(BlockFamilies.CUT_RED_SANDSTONE);
    public static final BlockFamily SMOOTH_RED_SANDSTONE = CONSTRUCTOR.blockFamily("smooth_red_sandstone", Blocks.SMOOTH_RED_SANDSTONE)
            .base(Blocks.SMOOTH_RED_SANDSTONE)
            .wall()
            .stairs(Blocks.SMOOTH_RED_SANDSTONE_STAIRS)
            .slab(Blocks.SMOOTH_RED_SANDSTONE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .smelting(BlockFamilies.RED_SANDSTONE)
            .build(BlockFamilies.SMOOTH_RED_SANDSTONE);
    public static final BlockFamily PRISMARINE_BRICK = CONSTRUCTOR.blockFamily("prismarine_brick", Blocks.PRISMARINE_BRICKS)
            .base(Blocks.PRISMARINE_BRICKS)
            .wall()
            .stairs(Blocks.PRISMARINE_BRICK_STAIRS)
            .slab(Blocks.PRISMARINE_BRICK_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.PRISMARINE_BRICK);
    public static final BlockFamily DARK_PRISMARINE = CONSTRUCTOR.blockFamily("dark_prismarine", Blocks.DARK_PRISMARINE)
            .base(Blocks.DARK_PRISMARINE)
            .wall()
            .stairs(Blocks.DARK_PRISMARINE_STAIRS)
            .slab(Blocks.DARK_PRISMARINE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build(BlockFamilies.DARK_PRISMARINE);
    public static final BlockFamily POLISHED_GRANITE = CONSTRUCTOR.blockFamily("polished_granite", Blocks.POLISHED_GRANITE)
            .base(Blocks.POLISHED_GRANITE)
            .wall()
            .stairs(Blocks.POLISHED_GRANITE_STAIRS)
            .slab(Blocks.POLISHED_GRANITE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.GRANITE)
            .build(BlockFamilies.POLISHED_GRANITE);
    public static final BlockFamily POLISHED_DIORITE = CONSTRUCTOR.blockFamily("polished_diorite", Blocks.POLISHED_DIORITE)
            .base(Blocks.POLISHED_DIORITE)
            .wall()
            .stairs(Blocks.POLISHED_DIORITE_STAIRS)
            .slab(Blocks.POLISHED_DIORITE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.DIORITE)
            .build(BlockFamilies.POLISHED_DIORITE);
    public static final BlockFamily POLISHED_ANDESITE = CONSTRUCTOR.blockFamily("polished_andesite", Blocks.POLISHED_ANDESITE)
            .base(Blocks.POLISHED_ANDESITE)
            .wall()
            .stairs(Blocks.POLISHED_ANDESITE_STAIRS)
            .slab(Blocks.POLISHED_ANDESITE_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting(Blocks.ANDESITE)
            .build(BlockFamilies.POLISHED_ANDESITE);
    public static final BlockFamily GILDED_BLACKSTONE = CONSTRUCTOR.blockFamily("gilded_blackstone", Blocks.GILDED_BLACKSTONE)
            .base(Blocks.GILDED_BLACKSTONE)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build();
    public static final BlockFamily CRACKED_POLISHED_BLACKSTONE_BRICK = CONSTRUCTOR.blockFamily("cracked_polished_blackstone_brick", Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
            .base(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .smelting(BlockFamilies.POLISHED_BLACKSTONE_BRICK)
            .build();
    public static final BlockFamily CRACKED_END_STONE_BRICK = CONSTRUCTOR.blockFamily("cracked_end_stone_brick", Blocks.END_STONE_BRICKS)
            .base()
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .smelting(BlockFamilies.END_STONE_BRICK)
            .build();
    public static final BlockFamily PURPUR = CONSTRUCTOR.blockFamily("purpur", Blocks.PURPUR_BLOCK)
            .base(Blocks.PURPUR_BLOCK)
            .wall()
            .stairs(Blocks.PURPUR_STAIRS)
            .slab(Blocks.PURPUR_SLAB)
            .tags(BlockTags.PICKAXE_MINEABLE)
            .stonecutting()
            .build();
    public static final BlockFamily CRACKED_PURPUR = CONSTRUCTOR.blockFamily("cracked_purpur", Blocks.PURPUR_BLOCK)
            .base()
            .wall()
            .stairs()
            .slab()
            .tags(BlockTags.PICKAXE_MINEABLE)
            .smelting(BlockFamilies.PURPUR)
            .build();

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
                    {PACKED_MUD},
                    {BlockFamilies.NETHER_BRICK, CRACKED_NETHER_BRICKS},
                    {BlockFamilies.RED_NETHER_BRICK, CRACKED_RED_NETHER_BRICKS},
                    {QUARTZ},
                    {SMOOTH_QUARTZ},
                    {QUARTZ_BRICK},
                    {BlockFamilies.SANDSTONE, CUT_SANDSTONE, SMOOTH_SANDSTONE},
                    {BlockFamilies.RED_SANDSTONE, CUT_RED_SANDSTONE, SMOOTH_RED_SANDSTONE},
                    {PRISMARINE_BRICK, DARK_PRISMARINE},
                    {BlockFamilies.GRANITE, POLISHED_GRANITE, GRANITE_BRICKS},
                    {BlockFamilies.DIORITE, POLISHED_DIORITE, DIORITE_BRICKS},
                    {BlockFamilies.ANDESITE, POLISHED_ANDESITE, ANDESITE_BRICKS},
                    {SNOW, SNOW_BRICKS},
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