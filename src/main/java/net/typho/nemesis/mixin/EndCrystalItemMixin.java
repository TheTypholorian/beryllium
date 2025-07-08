package net.typho.nemesis.mixin;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndCrystalItem.class)
@Implements(@Interface(iface = ProjectileItem.class, prefix = "proj$"))
public class EndCrystalItemMixin {
    public ProjectileEntity proj$createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        System.out.println("create proj");
        return new Nemesis.EndCrystalProjectileEntity(Nemesis.END_CRYSTAL_PROJECTILE_ENTITY, pos.getX(), pos.getY(), pos.getZ(), world, stack, null);
    }
}
