package net.typho.beryllium.mixin.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.typho.beryllium.combat.Combat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(
            method = "use",
            at = @At("HEAD")
    )
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);

        int dash = EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Combat.DASH_ENCHANTMENT).orElseThrow(), stack);

        if (dash > 0) {
            Vec3d look = user.getRotationVector();
            user.addVelocity(look.multiply(dash));
            user.velocityModified = true;
            user.getItemCooldownManager().set((Item) (Object) this, 200);
        }

        int reel = EnchantmentHelper.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Combat.REEL_ENCHANTMENT).orElseThrow(), stack);

        if (reel > 0) {
            Vec3d look = user.getRotationVector();
            Vec3d target = user.getPos().add(look.multiply(128));
            EntityHitResult hit = Combat.raycast(user, user.getPos(), target, new Box(user.getPos(), target).expand(1), entity -> !entity.isSpectator() && entity.canHit(), 128, 5);

            if (hit != null) {
                Entity reelTarget = hit.getEntity();
                reelTarget.addVelocity(look.multiply(-reel));
                reelTarget.velocityModified = true;
                user.getItemCooldownManager().set((Item) (Object) this, 200);
            }
        }
    }
}
