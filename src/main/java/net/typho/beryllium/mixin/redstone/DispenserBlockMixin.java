package net.typho.beryllium.mixin.redstone;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.typho.beryllium.config.ServerConfig;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
    @Unique
    private static final DispenserBehavior PLACE_BLOCK_BEHAVIOR = new FallibleItemDispenserBehavior() {
        @Override
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos targetPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            BlockState targetState = pointer.world().getBlockState(targetPos);

            if (stack.getItem() instanceof BlockItem blockItem && ServerConfig.DISPENSERS_PLACE_BLOCKS.get()) {
                Block block = blockItem.getBlock();
                BlockState placeState = block.getDefaultState();

                if (placeState != null && targetState.isReplaceable() && placeState.canPlaceAt(pointer.world(), targetPos)) {
                    pointer.world().setBlockState(targetPos, placeState);
                    BlockSoundGroup sounds = placeState.getSoundGroup();
                    pointer.world().playSound(
                            null,
                            targetPos,
                            sounds.getPlaceSound(),
                            SoundCategory.BLOCKS,
                            (sounds.getVolume() + 1) / 2,
                            sounds.getPitch() * 0.8f
                    );
                    pointer.world().emitGameEvent(GameEvent.BLOCK_PLACE, targetPos, GameEvent.Emitter.of(placeState));
                    stack.split(1);
                } else {
                    setSuccess(false);
                }
            }/* else if (stack.getItem() instanceof ToolItem && Beryllium.CONFIG.redstone.dispensersUseTools) {
                float hardness = targetState.getHardness(pointer.world(), targetPos);

                if (hardness != -1 && (!targetState.isToolRequired() || stack.isSuitableFor(targetState))) {
                    float delta = stack.getMiningSpeedMultiplier(targetState) / hardness / 30;

                    if (FabricLoader.getInstance().isModLoaded("multimine")) {
                        pointer.world().setBlockBreakingInfo(-1, targetPos, (int) (delta * 10));
                    } else {
                        if (delta >= 0.1) {
                            pointer.world().breakBlock(targetPos, true);

                            ItemStack[] r = {stack};
                            stack.damage(1, pointer.world(), null, broken -> r[0] = broken.getDefaultStack());
                            return r[0];
                        } else {
                            setSuccess(false);
                        }
                    }
                } else {
                    setSuccess(false);
                }
            }*/

            return stack;
        }
    };

    @Shadow
    @Final
    public static Map<Item, DispenserBehavior> BEHAVIORS = Util.make(new Object2ObjectOpenHashMap<>(), map -> map.defaultReturnValue(PLACE_BLOCK_BEHAVIOR));

    /**
     * @author The Typhothanian
     * @reason Dispensers placing blocks
     */
    @Overwrite
    public DispenserBehavior getBehaviorForItem(World world, ItemStack stack) {
        return !stack.isItemEnabled(world.getEnabledFeatures()) ? PLACE_BLOCK_BEHAVIOR : BEHAVIORS.get(stack.getItem());
    }
}
