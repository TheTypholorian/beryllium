package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.ArmorItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.food.Food;
import net.typho.beryllium.redstone.Redstone;
import net.typho.beryllium.util.BlockFamilyBuilder;

import java.util.List;

public class GenModels extends FabricModelProvider {
    public GenModels(FabricDataOutput output) {
        super(output);
    }

    public static void family(BlockStateModelGenerator gen, BlockFamily family) {
        BlockStateModelGenerator.BlockTexturePool pool = gen.registerCubeAllModelTexturePool(family.getBaseBlock());
        pool.family(family);
    }

    public void voidFire(BlockStateModelGenerator gen) {
        List<Identifier> voidFireFloor = gen.getFireFloorModels(Exploring.VOID_FIRE);
        List<Identifier> voidFireSide = gen.getFireSideModels(Exploring.VOID_FIRE);
        gen.blockStateCollector
                .accept(
                        MultipartBlockStateSupplier.create(Exploring.VOID_FIRE)
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireFloor, v -> v))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R90)))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R180)))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R270)))
                );
    }

    public void pointedBone(BlockStateModelGenerator gen) {
        gen.excludeFromSimpleItemModelGeneration(Exploring.POINTED_BONE);
        BlockStateVariantMap.DoubleProperty<Direction, Thickness> doubleProperty = BlockStateVariantMap.create(Properties.VERTICAL_DIRECTION, Properties.THICKNESS);

        for (Thickness thickness : Thickness.values()) {
            doubleProperty.register(Direction.UP, thickness, getBoneVariant(gen, Direction.UP, thickness));
        }

        for (Thickness thickness : Thickness.values()) {
            doubleProperty.register(Direction.DOWN, thickness, getBoneVariant(gen, Direction.DOWN, thickness));
        }

        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(Exploring.POINTED_BONE).coordinate(doubleProperty));
    }

    public void potionCauldron(BlockStateModelGenerator gen) {
        gen.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(Combat.POTION_CAULDRON)
                                .coordinate(
                                        BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL)
                                                .register(
                                                        1,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_LEVEL1
                                                                                .upload(Combat.POTION_CAULDRON, "_level1", TextureMap.cauldron(TextureMap.getSubId(Blocks.WATER, "_still")), gen.modelCollector)
                                                                )
                                                )
                                                .register(
                                                        2,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_LEVEL2
                                                                                .upload(Combat.POTION_CAULDRON, "_level2", TextureMap.cauldron(TextureMap.getSubId(Blocks.WATER, "_still")), gen.modelCollector)
                                                                )
                                                )
                                                .register(
                                                        3,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_FULL
                                                                                .upload(Combat.POTION_CAULDRON, "_full", TextureMap.cauldron(TextureMap.getSubId(Blocks.WATER, "_still")), gen.modelCollector)
                                                                )
                                                )
                                )
                );
    }

    public void goldHopper(BlockStateModelGenerator gen) {
        Identifier goldHopper = ModelIds.getBlockModelId(Redstone.GOLD_HOPPER_BLOCK);
        Identifier goldHopperSide = ModelIds.getBlockSubModelId(Redstone.GOLD_HOPPER_BLOCK, "_side");
        gen.registerItemModel(Redstone.GOLD_HOPPER_BLOCK.asItem());
        gen.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(Redstone.GOLD_HOPPER_BLOCK)
                                .coordinate(
                                        BlockStateVariantMap.create(Properties.HOPPER_FACING)
                                                .register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopper))
                                                .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide))
                                                .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                                .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                                .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, goldHopperSide).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                )
                );
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        family(gen, Building.MOSSY_STONE);
        family(gen, Building.CRACKED_STONE_BRICKS);
        family(gen, Building.SMOOTH_STONE);
        family(gen, Building.SNOW_BRICKS);

        for (BlockFamilyBuilder family : BlockFamilyBuilder.FAMILIES) {
            family(gen, family.build());
        }

        gen.registerFlowerbed(Exploring.DAFFODILS);
        gen.registerFlowerbed(Exploring.SCILLA);
        gen.registerFlowerbed(Exploring.GERANIUMS);
        gen.registerWallPlant(Exploring.ALGAE_BLOCK);

        gen.registerLog(Exploring.CORRUPTED_LOG)
                .log(Exploring.CORRUPTED_LOG)
                .wood(Exploring.CORRUPTED_WOOD);
        gen.registerLog(Exploring.STRIPPED_CORRUPTED_LOG)
                .log(Exploring.STRIPPED_CORRUPTED_LOG)
                .wood(Exploring.STRIPPED_CORRUPTED_WOOD);

        gen.registerSingleton(Exploring.CORRUPTED_END_STONE, TexturedModel.CUBE_BOTTOM_TOP);

        gen.registerTorch(Exploring.BLAZING_TORCH, Exploring.BLAZING_WALL_TORCH);

        gen.registerDispenserLikeOrientable(Redstone.DESTRUCTOR_BLOCK);

        voidFire(gen);
        pointedBone(gen);
        potionCauldron(gen);
        goldHopper(gen);
    }

    public BlockStateVariant getBoneVariant(BlockStateModelGenerator gen, Direction direction, Thickness thickness) {
        String string = "_" + direction.asString() + "_" + thickness.asString();
        TextureMap textureMap = TextureMap.cross(TextureMap.getSubId(Exploring.POINTED_BONE, string));
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, Models.POINTED_DRIPSTONE.upload(Exploring.POINTED_BONE, string, textureMap, gen.modelCollector));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        gen.register(Combat.DIAMOND_ARROW, Models.GENERATED);
        gen.register(Combat.IRON_ARROW, Models.GENERATED);
        gen.register(Combat.FLAMING_ARROW, Models.GENERATED);
        gen.register(Combat.COPPER_ARROW, Models.GENERATED);
        gen.register(Building.FILLING_WAND_ITEM, Models.GENERATED);
        gen.register(Exploring.FIREFLY_BOTTLE.asItem(), Models.GENERATED);
        gen.register(Food.CROISSANT, Models.GENERATED);
        gen.registerArmor((ArmorItem) Combat.NETHERITE_HORSE_ARMOR);
    }
}
