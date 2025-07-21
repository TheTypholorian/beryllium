package net.typho.beryllium.exploring;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class AlgaeItem extends Item {
    public final Block block;

    public AlgaeItem(Settings settings, Block block) {
        super(settings);
        this.block = block;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        BlockHitResult fluidHit = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        BlockHitResult solidHit = raycast(world, user, RaycastContext.FluidHandling.NONE);
        BlockHitResult hit = solidHit.getBlockPos() != null && world.getBlockState(solidHit.getBlockPos()).isOf(Blocks.WATER) ? fluidHit : solidHit;
        BlockHitResult place = hit.withBlockPos(hit.getBlockPos().offset(hit.getSide()));
        ItemStack stack = user.getStackInHand(hand);
        ItemPlacementContext placement = new ItemPlacementContext(user, hand, stack, place);

        if (placement.canPlace()) {
            BlockState state = block.getPlacementState(placement);

            if (state != null && state.canPlaceAt(world, place.getBlockPos())) {
                world.setBlockState(placement.getBlockPos(), state);
                stack.decrementUnlessCreative(1, user);
                BlockSoundGroup sounds = state.getSoundGroup();
                world.playSound(
                        user,
                        placement.getBlockPos(),
                        sounds.getPlaceSound(),
                        SoundCategory.BLOCKS,
                        (sounds.getVolume() + 1) / 2,
                        sounds.getPitch() * 0.8f
                );
                world.emitGameEvent(GameEvent.BLOCK_PLACE, placement.getBlockPos(), GameEvent.Emitter.of(user, state));

                return new TypedActionResult<>(ActionResult.CONSUME, stack);
            }
        }

        return super.use(world, user, hand);
    }
}
