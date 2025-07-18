package net.typho.beryllium.building;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.BerylliumModule;
import net.typho.beryllium.building.kiln.KilnBlock;

import static net.typho.beryllium.BerylliumModule.*;

public class Building implements BerylliumModule {
    public static final Block KILN_BLOCK = block("kiln", new KilnBlock(AbstractBlock.Settings.copy(Blocks.BLAST_FURNACE)));
    public static final Item KILN_BLOCK_ITEM = item("kiln", new BlockItem(KILN_BLOCK, new Item.Settings()));

    private static <T extends BlockEntity> BlockEntityType<T> blockEntity(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Beryllium.MOD_ID, id), builder.build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id)));
    }

    public static final BlockEntityType<KilnBlock.Entity> KILN_BLOCK_ENTITY_TYPE = blockEntity("kiln", BlockEntityType.Builder.create(KilnBlock.Entity::new, KILN_BLOCK));

    public static final BlockFamily MOSSY_STONE = new BlockFamily.Builder(block("mossy_stone", new Block(AbstractBlock.Settings.copy(Blocks.STONE))))
            .wall(block("mossy_stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE))))
            .stairs(block("mossy_stone_stairs", new StairsBlock(Blocks.STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE))))
            .slab(block("mossy_stone_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE))))
            .build();
    public static final BlockFamily CRACKED_STONE_BRICKS = new BlockFamily.Builder(Blocks.CRACKED_STONE_BRICKS)
            .wall(block("cracked_stone_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))))
            .stairs(block("cracked_stone_brick_stairs", new StairsBlock(Blocks.STONE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))))
            .slab(block("cracked_stone_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS))))
            .build();
    public static final BlockFamily SMOOTH_STONE = new BlockFamily.Builder(Blocks.SMOOTH_STONE)
            .chiseled(block("chiseled_smooth_stone", new Block(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE))))
            .wall(block("smooth_stone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE))))
            .stairs(block("smooth_stone_stairs", new StairsBlock(Blocks.SMOOTH_STONE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE))))
            .slab(Blocks.SMOOTH_STONE_SLAB)
            .build();

    @Override
    public void onInitialize() {
        /*
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
                .register(entries -> {
                    entries.addAfter(
                            Items.STONE_BUTTON,
                            MOSSY_STONE.getBaseBlock().asItem(),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.STAIRS),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.SLAB),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.PRESSURE_PLATE),
                            MOSSY_STONE.getVariant(BlockFamily.Variant.BUTTON)
                    );
                    entries.addAfter(
                            Items.MOSSY_STONE_BRICKS,
                            CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.CHISELED),
                            CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.STAIRS),
                            CRACKED_STONE_BRICKS.getVariant(BlockFamily.Variant.SLAB)
                    );
                });
         */
        HandledScreens.register(KilnBlock.SCREEN_HANDLER_TYPE, KilnBlock.Screen::new);
        /*
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated, environment) -> dispatcher.register(
                        CommandManager.literal("placeblocksets")
                                .requires(serverSource -> serverSource.hasPermissionLevel(2))
                                .executes(ctx -> {
                                    Vec3d srcPos = ctx.getSource().getPosition();
                                    BlockPos.Mutable place = new BlockPos.Mutable(srcPos.x, srcPos.y, srcPos.z);

                                    for (BlockSet.Type type : BlockSet.Type.values()) {
                                        for (BlockSet set : type.map.values()) {
                                            if (!set.place(ctx.getSource().getWorld(), place)) {
                                                return 0;
                                            }

                                            place.setZ(place.getZ() + 1);
                                            place.setX(MathHelper.floor(srcPos.x));
                                        }
                                    }

                                    return 1;
                                })
                )
        );
         */
    }
}
    /*
    public static final BlockSet OAK_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("oak"))
            .setSolid(Blocks.OAK_PLANKS)
            .genChiseled()
            .setStairs(Blocks.OAK_STAIRS)
            .setSlab(Blocks.OAK_SLAB)
            .setFence(Blocks.OAK_FENCE)
            .setFenceGate(Blocks.OAK_FENCE_GATE)
            .setDoor(Blocks.OAK_DOOR)
            .setTrapdoor(Blocks.OAK_TRAPDOOR)
            .setPressurePlate(Blocks.OAK_PRESSURE_PLATE)
            .setButton(Blocks.OAK_BUTTON)
            .setSign(Blocks.OAK_SIGN)
            .setWallSign(Blocks.OAK_WALL_SIGN);

    public static final BlockSet SPRUCE_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("spruce"))
            .setSolid(Blocks.SPRUCE_PLANKS)
            .genChiseled()
            .setStairs(Blocks.SPRUCE_STAIRS)
            .setSlab(Blocks.SPRUCE_SLAB)
            .setFence(Blocks.SPRUCE_FENCE)
            .setFenceGate(Blocks.SPRUCE_FENCE_GATE)
            .setDoor(Blocks.SPRUCE_DOOR)
            .setTrapdoor(Blocks.SPRUCE_TRAPDOOR)
            .setPressurePlate(Blocks.SPRUCE_PRESSURE_PLATE)
            .setButton(Blocks.SPRUCE_BUTTON)
            .setSign(Blocks.SPRUCE_SIGN)
            .setWallSign(Blocks.SPRUCE_WALL_SIGN);

    public static final BlockSet BIRCH_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("birch"))
            .setSolid(Blocks.BIRCH_PLANKS)
            .genChiseled()
            .setStairs(Blocks.BIRCH_STAIRS)
            .setSlab(Blocks.BIRCH_SLAB)
            .setFence(Blocks.BIRCH_FENCE)
            .setFenceGate(Blocks.BIRCH_FENCE_GATE)
            .setDoor(Blocks.BIRCH_DOOR)
            .setTrapdoor(Blocks.BIRCH_TRAPDOOR)
            .setPressurePlate(Blocks.BIRCH_PRESSURE_PLATE)
            .setButton(Blocks.BIRCH_BUTTON)
            .setSign(Blocks.BIRCH_SIGN)
            .setWallSign(Blocks.BIRCH_WALL_SIGN);

    public static final BlockSet JUNGLE_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("jungle"))
            .setSolid(Blocks.JUNGLE_PLANKS)
            .genChiseled()
            .setStairs(Blocks.JUNGLE_STAIRS)
            .setSlab(Blocks.JUNGLE_SLAB)
            .setFence(Blocks.JUNGLE_FENCE)
            .setFenceGate(Blocks.JUNGLE_FENCE_GATE)
            .setDoor(Blocks.JUNGLE_DOOR)
            .setTrapdoor(Blocks.JUNGLE_TRAPDOOR)
            .setPressurePlate(Blocks.JUNGLE_PRESSURE_PLATE)
            .setButton(Blocks.JUNGLE_BUTTON)
            .setSign(Blocks.JUNGLE_SIGN)
            .setWallSign(Blocks.JUNGLE_WALL_SIGN);

    public static final BlockSet ACACIA_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("acacia"))
            .setSolid(Blocks.ACACIA_PLANKS)
            .genChiseled()
            .setStairs(Blocks.ACACIA_STAIRS)
            .setSlab(Blocks.ACACIA_SLAB)
            .setFence(Blocks.ACACIA_FENCE)
            .setFenceGate(Blocks.ACACIA_FENCE_GATE)
            .setDoor(Blocks.ACACIA_DOOR)
            .setTrapdoor(Blocks.ACACIA_TRAPDOOR)
            .setPressurePlate(Blocks.ACACIA_PRESSURE_PLATE)
            .setButton(Blocks.ACACIA_BUTTON)
            .setSign(Blocks.ACACIA_SIGN)
            .setWallSign(Blocks.ACACIA_WALL_SIGN);

    public static final BlockSet DARK_OAK_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("dark_oak"))
            .setSolid(Blocks.DARK_OAK_PLANKS)
            .genChiseled()
            .setStairs(Blocks.DARK_OAK_STAIRS)
            .setSlab(Blocks.DARK_OAK_SLAB)
            .setFence(Blocks.DARK_OAK_FENCE)
            .setFenceGate(Blocks.DARK_OAK_FENCE_GATE)
            .setDoor(Blocks.DARK_OAK_DOOR)
            .setTrapdoor(Blocks.DARK_OAK_TRAPDOOR)
            .setPressurePlate(Blocks.DARK_OAK_PRESSURE_PLATE)
            .setButton(Blocks.DARK_OAK_BUTTON)
            .setSign(Blocks.DARK_OAK_SIGN)
            .setWallSign(Blocks.DARK_OAK_WALL_SIGN);

    public static final BlockSet MANGROVE_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("mangrove"))
            .setSolid(Blocks.MANGROVE_PLANKS)
            .genChiseled()
            .setStairs(Blocks.MANGROVE_STAIRS)
            .setSlab(Blocks.MANGROVE_SLAB)
            .setFence(Blocks.MANGROVE_FENCE)
            .setFenceGate(Blocks.MANGROVE_FENCE_GATE)
            .setDoor(Blocks.MANGROVE_DOOR)
            .setTrapdoor(Blocks.MANGROVE_TRAPDOOR)
            .setPressurePlate(Blocks.MANGROVE_PRESSURE_PLATE)
            .setButton(Blocks.MANGROVE_BUTTON)
            .setSign(Blocks.MANGROVE_SIGN)
            .setWallSign(Blocks.MANGROVE_WALL_SIGN);

    public static final BlockSet CHERRY_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("cherry"))
            .setSolid(Blocks.CHERRY_PLANKS)
            .genChiseled()
            .setStairs(Blocks.CHERRY_STAIRS)
            .setSlab(Blocks.CHERRY_SLAB)
            .setFence(Blocks.CHERRY_FENCE)
            .setFenceGate(Blocks.CHERRY_FENCE_GATE)
            .setDoor(Blocks.CHERRY_DOOR)
            .setTrapdoor(Blocks.CHERRY_TRAPDOOR)
            .setPressurePlate(Blocks.CHERRY_PRESSURE_PLATE)
            .setButton(Blocks.CHERRY_BUTTON)
            .setSign(Blocks.CHERRY_SIGN)
            .setWallSign(Blocks.CHERRY_WALL_SIGN);

    public static final BlockSet BAMBOO_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("bamboo"))
            .setSolid(Blocks.BAMBOO_PLANKS)
            .genChiseled()
            .setStairs(Blocks.BAMBOO_STAIRS)
            .setSlab(Blocks.BAMBOO_SLAB)
            .setFence(Blocks.BAMBOO_FENCE)
            .setFenceGate(Blocks.BAMBOO_FENCE_GATE)
            .setDoor(Blocks.BAMBOO_DOOR)
            .setTrapdoor(Blocks.BAMBOO_TRAPDOOR)
            .setPressurePlate(Blocks.BAMBOO_PRESSURE_PLATE)
            .setButton(Blocks.BAMBOO_BUTTON)
            .setSign(Blocks.BAMBOO_SIGN)
            .setWallSign(Blocks.BAMBOO_WALL_SIGN);

    public static final BlockSet BAMBOO_MOSAIC_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("bamboo_mosaic"))
            .setSolid(Blocks.BAMBOO_MOSAIC)
            .setStairs(Blocks.BAMBOO_MOSAIC_STAIRS)
            .setSlab(Blocks.BAMBOO_MOSAIC_SLAB);

    public static final BlockSet CRIMSON_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("crimson"))
            .setSolid(Blocks.CRIMSON_PLANKS)
            .genChiseled()
            .setStairs(Blocks.CRIMSON_STAIRS)
            .setSlab(Blocks.CRIMSON_SLAB)
            .setFence(Blocks.CRIMSON_FENCE)
            .setFenceGate(Blocks.CRIMSON_FENCE_GATE)
            .setDoor(Blocks.CRIMSON_DOOR)
            .setTrapdoor(Blocks.CRIMSON_TRAPDOOR)
            .setPressurePlate(Blocks.CRIMSON_PRESSURE_PLATE)
            .setButton(Blocks.CRIMSON_BUTTON)
            .setSign(Blocks.CRIMSON_SIGN)
            .setWallSign(Blocks.CRIMSON_WALL_SIGN);

    public static final BlockSet WARPED_BLOCK_SET = new BlockSet(BlockSet.Type.WOOD, Identifier.of("warped"))
            .setSolid(Blocks.WARPED_PLANKS)
            .genChiseled()
            .setStairs(Blocks.WARPED_STAIRS)
            .setSlab(Blocks.WARPED_SLAB)
            .setFence(Blocks.WARPED_FENCE)
            .setFenceGate(Blocks.WARPED_FENCE_GATE)
            .setDoor(Blocks.WARPED_DOOR)
            .setTrapdoor(Blocks.WARPED_TRAPDOOR)
            .setPressurePlate(Blocks.WARPED_PRESSURE_PLATE)
            .setButton(Blocks.WARPED_BUTTON)
            .setSign(Blocks.WARPED_SIGN)
            .setWallSign(Blocks.WARPED_WALL_SIGN);

    public static final BlockSet COBBLESTONE_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of("cobblestone"))
            .setSolid(Blocks.COBBLESTONE)
            .setStairs(Blocks.COBBLESTONE_STAIRS)
            .setSlab(Blocks.COBBLESTONE_SLAB)
            .setWall(Blocks.COBBLESTONE_WALL);

    public static final BlockSet STONE_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of("stone"))
            .setSolid(Blocks.STONE)
            .setStairs(Blocks.STONE_STAIRS)
            .setSlab(Blocks.STONE_SLAB)
            .genWall()
            .setPressurePlate(Blocks.STONE_PRESSURE_PLATE);

    public static final BlockSet MOSSY_STONE_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of(Beryllium.MOD_ID, "mossy_stone"))
            .genSolid(Blocks.STONE)
            .genStairs()
            .genSlab()
            .genWall()
            .genPressurePlate(BlockSetType.STONE);

    public static final BlockSet SMOOTH_STONE_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of("smooth_stone"))
            .setSolid(Blocks.SMOOTH_STONE)
            .genStairs()
            .setSlab(Blocks.SMOOTH_STONE_SLAB)
            .genWall();

    public static final BlockSet QUARTZ_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of("quartz"))
            .setSolid(Blocks.QUARTZ_BLOCK)
            .setPillar(Blocks.QUARTZ_PILLAR)
            .setStairs(Blocks.QUARTZ_STAIRS)
            .setSlab(Blocks.QUARTZ_SLAB)
            .genWall();

    public static final BlockSet QUARTZ_BRICKS_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of("quartz_bricks"))
            .setSolid(Blocks.QUARTZ_BRICKS)
            .genStairs()
            .genSlab()
            .genWall();

    public static final BlockSet SMOOTH_QUARTZ_BLOCK_SET = new BlockSet(BlockSet.Type.ROCKS, Identifier.of("smooth_quartz"))
            .setSolid(Blocks.SMOOTH_QUARTZ)
            .setStairs(Blocks.SMOOTH_QUARTZ_STAIRS)
            .setSlab(Blocks.SMOOTH_QUARTZ_SLAB)
            .genWall();
     */
