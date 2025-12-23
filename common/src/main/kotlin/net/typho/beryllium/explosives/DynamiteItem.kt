package net.typho.beryllium.explosives

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level

class DynamiteItem(properties: Properties) : Item(properties), ProjectileItem {
    override fun asProjectile(
        level: Level,
        pos: Position,
        stack: ItemStack,
        direction: Direction
    ): Projectile {
        TODO()
    }
}