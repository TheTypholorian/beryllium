package net.typho.beryllium.crossbows

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.windcharge.WindCharge
import net.minecraft.world.item.CrossbowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.typho.beryllium.ModConfig
import java.util.function.Predicate

class BurstCrossbowItem(properties: Properties) : CrossbowItem(properties) {
    override fun performShooting(
        level: Level,
        shooter: LivingEntity,
        hand: InteractionHand,
        weapon: ItemStack,
        velocity: Float,
        inaccuracy: Float,
        target: LivingEntity?
    ) {
        super.performShooting(level, shooter, hand, weapon, velocity * ModConfig.instance.crossbows.burstCrossbow.velocityMultiplier.get(), inaccuracy, target)
    }

    override fun getSupportedHeldProjectiles(): Predicate<ItemStack?> {
        return super.getSupportedHeldProjectiles().or { stack -> ModConfig.instance.crossbows.burstCrossbow.canLoadWindCharges && stack.`is`(Items.WIND_CHARGE) }
    }

    override fun createProjectile(
        level: Level,
        shooter: LivingEntity,
        weapon: ItemStack,
        ammo: ItemStack,
        isCrit: Boolean
    ): Projectile {
        if (ammo.`is`(Items.WIND_CHARGE)) {
            return WindCharge(level, shooter.x, shooter.eyeY - 0.15f, shooter.z, Vec3(0.0, 0.0, 0.0))
        }

        return super.createProjectile(level, shooter, weapon, ammo, isCrit)
    }
}