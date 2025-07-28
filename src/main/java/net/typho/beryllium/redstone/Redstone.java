package net.typho.beryllium.redstone;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.typho.beryllium.Module;

public class Redstone extends Module {
    public final Block GOLD_HOPPER_BLOCK = blockWithItem("gold_hopper", new GoldHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), new Item.Settings());
    public final BlockEntityType<GoldHopperBlockEntity> GOLD_HOPPER_BLOCK_ENTITY = blockEntity("gold_hopper", BlockEntityType.Builder.create(GoldHopperBlockEntity::new, GOLD_HOPPER_BLOCK));
    public final ScreenHandlerType<GoldHopperScreenHandler> GOLD_HOPPER_SCREEN_HANDLER_TYPE = screenHandler("gold_hopper", GoldHopperScreenHandler::new);

    public Redstone(String name) {
        super(name);
    }

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
