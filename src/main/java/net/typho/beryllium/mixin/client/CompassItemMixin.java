package net.typho.beryllium.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
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

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && player.getInventory().contains(stack)) {
            BlockPos playerPos = player.getBlockPos();
            tooltip.add(Text.translatable("item.beryllium.exploring.compass.pos", playerPos.getX(), playerPos.getY(), playerPos.getZ()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        }
    }
}
