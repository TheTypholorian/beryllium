package net.typho.beryllium.mixin.combat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PotionItem;
import net.minecraft.util.ActionResult;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.combat.PotionCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V"
            ),
            index = 0
    )
    private static Item.Settings applyMaxCount(Item.Settings settings) {
        return settings.maxCount(16);
    }

    @Inject(
            method = "useOnBlock",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            ),
            cancellable = true
    )
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());

        if (blockState.isOf(Blocks.CAULDRON) || blockState.isOf(Combat.POTION_CAULDRON)) {
            if (blockState.getOrEmpty(LeveledCauldronBlock.LEVEL).orElse(0) < LeveledCauldronBlock.MAX_LEVEL) {
                cir.setReturnValue(PotionCauldronBlock.BEHAVIOR.interact(blockState, context.getWorld(), context.getBlockPos(), context.getPlayer(), context.getHand(), context.getStack()).toActionResult());
            }
        }
    }
}