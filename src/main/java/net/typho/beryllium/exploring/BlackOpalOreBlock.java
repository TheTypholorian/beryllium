package net.typho.beryllium.exploring;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public class BlackOpalOreBlock extends Block {
    public static final IntProperty STAGE = IntProperty.of("stage", 0, 3);

    public BlackOpalOreBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}
