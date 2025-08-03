package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.combat.EndCrystalProjectileEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
@Implements(@Interface(iface = ProjectileItem.class, prefix = "proj$"))
public class EndCrystalItemMixin {
    public ProjectileEntity proj$createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new EndCrystalProjectileEntity(Combat.END_CRYSTAL_PROJECTILE_ENTITY, pos.getX(), pos.getY() - 1, pos.getZ(), world, stack, null);
    }

    @Inject(
            method = "useOnBlock",
            at = @At("RETURN")
    )
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue().isAccepted()) {
            if (context.getPlayer() != null) {
                context.getPlayer().getItemCooldownManager().set((Item) (Object) this, Beryllium.SERVER_CONFIG.endCrystalCooldown.get());
            }
        }
    }
}
