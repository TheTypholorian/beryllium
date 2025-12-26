package net.typho.beryllium

import net.minecraft.Util
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.*
import net.minecraft.world.item.component.ChargedProjectiles
import net.minecraft.world.item.crafting.Ingredient
import net.typho.beryllium.crossbows.BurstCrossbowItem
import net.typho.beryllium.explosives.DynamiteItem
import net.typho.beryllium.mixin.accessors.CreativeModeTabsAccessor
import net.typho.beryllium.pillagers.RavagerShieldItem
import net.typho.beryllium.platform.Services
import java.util.*
import java.util.function.Consumer

object ModItems {
    fun init() = Unit

    val levitationArmorMaterial = Services.REGISTRAR.registerArmorMaterial("levitation") {
        ArmorMaterial(
            Util.make(
                EnumMap(ArmorItem.Type::class.java),
                Consumer { map ->
                    map.put(ArmorItem.Type.BOOTS, 2)
                    map.put(ArmorItem.Type.LEGGINGS, 5)
                    map.put(ArmorItem.Type.CHESTPLATE, 6)
                    map.put(ArmorItem.Type.HELMET, 2)
                    map.put(ArmorItem.Type.BODY, 5)
                }),
            9,
            SoundEvents.BREEZE_WIND_CHARGE_BURST,
            { Ingredient.of(Items.WIND_CHARGE) },
            listOf(ArmorMaterial.Layer(Beryllium.id("levitation"))),
            0f,
            0f
        )
    }

    @JvmField
    val burstCrossbow = Services.REGISTRAR.registerItem("burst_crossbow") { properties ->
        BurstCrossbowItem(
            properties
                .stacksTo(1)
                .durability(465)
                .component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)
        )
    }
    @JvmField
    val dynamite = Services.REGISTRAR.registerItem("dynamite") { properties ->
        DynamiteItem(properties)
    }
    @JvmField
    val levitationBoots = Services.REGISTRAR.registerItem("levitation_boots") { properties ->
        ArmorItem(
            levitationArmorMaterial.get(),
            ArmorItem.Type.BOOTS,
            properties.durability(ArmorItem.Type.BOOTS.getDurability(15))
        )
    }
    @JvmField
    val guster = Services.REGISTRAR.registerItem("guster") { properties ->
        BlockItem(
            ModBlocks.guster.get(),
            properties
        )
    }
    @JvmField
    val ravagerShield = Services.REGISTRAR.registerItem("ravager_shield") { properties ->
        RavagerShieldItem(
            properties
        )
    }

    @JvmStatic
    fun getCreativeTabContents(key: ResourceKey<CreativeModeTab>): List<ItemStack>? {
        if (key == CreativeModeTabsAccessor.getCombatTab()) {
            return listOf(
                ItemStack(burstCrossbow.get()),
                ItemStack(levitationBoots.get()),
            )
        } else if (key == CreativeModeTabsAccessor.getToolsTab()) {
            return listOf(
                ItemStack(dynamite.get())
            )
        }

        return null
    }
}