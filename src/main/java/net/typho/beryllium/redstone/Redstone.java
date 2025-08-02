package net.typho.beryllium.redstone;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.typho.beryllium.Constructor;

public class Redstone implements ModInitializer {
    public static final Constructor CONSTRUCTOR = new Constructor("redstone");

    public static final Block GOLD_HOPPER_BLOCK = CONSTRUCTOR.blockWithItem("gold_hopper", new GoldHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), new Item.Settings());
    public static final BlockEntityType<GoldHopperBlockEntity> GOLD_HOPPER_BLOCK_ENTITY = CONSTRUCTOR.blockEntity("gold_hopper", BlockEntityType.Builder.create(GoldHopperBlockEntity::new, GOLD_HOPPER_BLOCK));
    public static final ScreenHandlerType<GoldHopperScreenHandler> GOLD_HOPPER_SCREEN_HANDLER_TYPE = CONSTRUCTOR.screenHandler("gold_hopper", GoldHopperScreenHandler::new);

    @Override
    public void onInitialize() {
        HandledScreens.register(GOLD_HOPPER_SCREEN_HANDLER_TYPE, GoldHopperScreen::new);
    }

    public static class Config extends ConfigSection {
        public boolean cropComparatorOutput = true;
        public boolean dispensersPlaceBlocks = true;
        public boolean dispensersUseTools = true;
        public int hopperCooldown = 1;
        public boolean instantChainTNT = true;
    }
}
