package net.typho.beryllium.mixin.exploring;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.typho.beryllium.exploring.Exploring;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleItemMixin {
    @Shadow
    protected abstract ItemStack fill(ItemStack stack, PlayerEntity player, ItemStack outputStack);

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/TypedActionResult;pass(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, @Local ItemStack stack) {
        if (world.getAmbientDarkness() < 4 && world.getBiome(user.getBlockPos()).isIn(Exploring.HAS_FIREFLIES)) {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GLASS_HIT, SoundCategory.NEUTRAL, 1, 1);
            cir.setReturnValue(TypedActionResult.success(fill(stack, user, new ItemStack(Exploring.FIREFLY_BOTTLE)), true));
        }
    }
}
