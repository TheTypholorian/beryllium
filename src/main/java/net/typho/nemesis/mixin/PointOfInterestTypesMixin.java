package net.typho.nemesis.mixin;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public class PointOfInterestTypesMixin {
    @Redirect(
            method = "registerAndGetDefault(Lnet/minecraft/registry/Registry;)Lnet/minecraft/world/poi/PointOfInterestType;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/poi/PointOfInterestTypes;getStatesOfBlock(Lnet/minecraft/block/Block;)Ljava/util/Set;"
            )
    )
    private static Set<BlockState> redirectGetStatesOfBlock(Block block) {
        if (block == Blocks.LECTERN) {
            return ImmutableSet.copyOf(Blocks.ENCHANTING_TABLE.getStateManager().getStates());
        }

        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }
}
