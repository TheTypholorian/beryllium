package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.typho.beryllium.Beryllium;

import java.util.List;

public class GenModels extends FabricModelProvider {
    public GenModels(FabricDataOutput output) {
        super(output);
    }

    public static void family(BlockStateModelGenerator gen, BlockFamily family) {
        BlockStateModelGenerator.BlockTexturePool pool = gen.registerCubeAllModelTexturePool(family.getBaseBlock());
        pool.family(family);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        family(gen, Beryllium.BUILDING.MOSSY_STONE);
        family(gen, Beryllium.BUILDING.CRACKED_STONE_BRICKS);
        family(gen, Beryllium.BUILDING.SMOOTH_STONE);
        family(gen, Beryllium.BUILDING.SNOW_BRICKS);

        gen.registerFlowerbed(Beryllium.EXPLORING.DAFFODILS);
        gen.registerFlowerbed(Beryllium.EXPLORING.SCILLA);
        gen.registerFlowerbed(Beryllium.EXPLORING.GERANIUMS);
        gen.registerWallPlant(Beryllium.EXPLORING.ALGAE_BLOCK);

        gen.registerLog(Beryllium.EXPLORING.CORRUPTED_LOG)
                .log(Beryllium.EXPLORING.CORRUPTED_LOG);

        gen.registerSingleton(Beryllium.EXPLORING.CORRUPTED_END_STONE, TexturedModel.CUBE_BOTTOM_TOP);

        Identifier goldHopper = ModelIds.getBlockModelId(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK);
        Identifier goldHopperSide = ModelIds.getBlockSubModelId(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK, "_side");
        gen.registerItemModel(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK.asItem());
        gen.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(Beryllium.REDSTONE.GOLD_HOPPER_BLOCK)
                                .coordinate(
                                        BlockStateVariantMap.create(Properties.HOPPER_FACING)
                                                .register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopper))
                                                .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide))
                                                .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                                .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                                .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                )
                );

        List<Identifier> voidFireFloor = gen.getFireFloorModels(Beryllium.EXPLORING.VOID_FIRE);
        List<Identifier> voidFireSide = gen.getFireSideModels(Beryllium.EXPLORING.VOID_FIRE);
        gen.blockStateCollector
                .accept(
                        MultipartBlockStateSupplier.create(Beryllium.EXPLORING.VOID_FIRE)
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireFloor, v -> v))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R90)))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R180)))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R270)))
                );

        gen.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(Beryllium.COMBAT.POTION_CAULDRON)
                                .coordinate(
                                        BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL)
                                                .register(
                                                        1,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_LEVEL1
                                                                                .upload(Beryllium.COMBAT.POTION_CAULDRON, "_level1", TextureMap.cauldron(TextureMap.getSubId(Blocks.WATER, "_still")), gen.modelCollector)
                                                                )
                                                )
                                                .register(
                                                        2,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_LEVEL2
                                                                                .upload(Beryllium.COMBAT.POTION_CAULDRON, "_level2", TextureMap.cauldron(TextureMap.getSubId(Blocks.WATER, "_still")), gen.modelCollector)
                                                                )
                                                )
                                                .register(
                                                        3,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_FULL
                                                                                .upload(Beryllium.COMBAT.POTION_CAULDRON, "_full", TextureMap.cauldron(TextureMap.getSubId(Blocks.WATER, "_still")), gen.modelCollector)
                                                                )
                                                )
                                )
                );

        gen.excludeFromSimpleItemModelGeneration(Beryllium.EXPLORING.POINTED_BONE);
        BlockStateVariantMap.DoubleProperty<Direction, Thickness> doubleProperty = BlockStateVariantMap.create(Properties.VERTICAL_DIRECTION, Properties.THICKNESS);

        for (Thickness thickness : Thickness.values()) {
            doubleProperty.register(Direction.UP, thickness, getBoneVariant(gen, Direction.UP, thickness));
        }

        for (Thickness thickness : Thickness.values()) {
            doubleProperty.register(Direction.DOWN, thickness, getBoneVariant(gen, Direction.DOWN, thickness));
        }

        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(Beryllium.EXPLORING.POINTED_BONE).coordinate(doubleProperty));
    }

    public BlockStateVariant getBoneVariant(BlockStateModelGenerator gen, Direction direction, Thickness thickness) {
        String string = "_" + direction.asString() + "_" + thickness.asString();
        TextureMap textureMap = TextureMap.cross(TextureMap.getSubId(Beryllium.EXPLORING.POINTED_BONE, string));
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, Models.POINTED_DRIPSTONE.upload(Beryllium.EXPLORING.POINTED_BONE, string, textureMap, gen.modelCollector));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        gen.register(Beryllium.COMBAT.DIAMOND_ARROW, Models.GENERATED);
        gen.register(Beryllium.COMBAT.IRON_ARROW, Models.GENERATED);
        gen.register(Beryllium.COMBAT.FLAMING_ARROW, Models.GENERATED);
        gen.register(Beryllium.COMBAT.COPPER_ARROW, Models.GENERATED);
        gen.register(Beryllium.BUILDING.MAGIC_WAND_ITEM, Models.GENERATED);
        gen.register(Beryllium.EXPLORING.FIREFLY_BOTTLE.asItem(), Models.GENERATED);
        gen.register(Beryllium.FOOD.CROISSANT, Models.GENERATED);
    }
}
