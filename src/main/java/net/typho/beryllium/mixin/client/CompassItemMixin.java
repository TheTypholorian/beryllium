package net.typho.beryllium.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.typho.beryllium.config.BerylliumConfig;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(CompassItem.class)
public abstract class CompassItemMixin extends Item {
    public CompassItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        if (stack.getHolder() != null && stack.getHolder() instanceof PlayerEntity player && BerylliumConfig.COMPASS_COORDS.get() && !player.hasReducedDebugInfo()) {
            BlockPos playerPos = stack.getHolder().getBlockPos();
            tooltip.add(Text.translatable("item.beryllium.exploring.compass.pos", playerPos.getX(), playerPos.getY(), playerPos.getZ()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        }
    }
}
