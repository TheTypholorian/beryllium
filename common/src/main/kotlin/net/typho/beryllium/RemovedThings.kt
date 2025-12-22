package net.typho.beryllium

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.ShapedRecipe

object RemovedThings {
    @JvmStatic
    val items = listOf(
        // keep wooden tools

        Items.STONE_SWORD,
        Items.STONE_AXE,
        Items.STONE_PICKAXE,
        Items.STONE_SHOVEL,
        Items.STONE_HOE,

        // keep iron tools

        Items.GOLDEN_SWORD,
        Items.GOLDEN_AXE,
        Items.GOLDEN_PICKAXE,
        Items.GOLDEN_SHOVEL,
        Items.GOLDEN_HOE,

        Items.DIAMOND_SWORD,
        Items.DIAMOND_AXE,
        Items.DIAMOND_PICKAXE,
        Items.DIAMOND_SHOVEL,
        Items.DIAMOND_HOE,

        Items.NETHERITE_SWORD,
        Items.NETHERITE_AXE,
        Items.NETHERITE_PICKAXE,
        Items.NETHERITE_SHOVEL,
        Items.NETHERITE_HOE,

        // keep leather armor
        Items.LEATHER_HORSE_ARMOR,

        Items.CHAINMAIL_HELMET,
        Items.CHAINMAIL_CHESTPLATE,
        Items.CHAINMAIL_LEGGINGS,
        Items.CHAINMAIL_BOOTS,
        // mojang where chainmail horse armor

        // keep iron armor
        Items.IRON_HORSE_ARMOR,

        Items.GOLDEN_HELMET,
        Items.GOLDEN_CHESTPLATE,
        Items.GOLDEN_LEGGINGS,
        Items.GOLDEN_BOOTS,
        Items.GOLDEN_HORSE_ARMOR,

        Items.DIAMOND_HELMET,
        Items.DIAMOND_CHESTPLATE,
        Items.DIAMOND_LEGGINGS,
        Items.DIAMOND_BOOTS,
        Items.DIAMOND_HORSE_ARMOR,

        Items.NETHERITE_HELMET,
        Items.NETHERITE_CHESTPLATE,
        Items.NETHERITE_LEGGINGS,
        Items.NETHERITE_BOOTS,
        // mojang where netherite horse armor

        Items.MACE,
        Items.WOLF_ARMOR,

        Items.ENCHANTING_TABLE,
        Items.ENCHANTED_BOOK,
    )

    @JvmStatic
    fun isSmithingTemplateRecipe(shaped: ShapedRecipe, registries: HolderLookup.Provider): Boolean {
        val ingredients = shaped.ingredients

        if (ingredients.size == 9) {
            val diamonds = intArrayOf(0, 2, 3, 5, 6, 7, 8)

            for (index in diamonds) {
                if (!ingredients[index].test(ItemStack(Items.DIAMOND))) {
                    return false
                }
            }

            if (ingredients[1].test(shaped.getResultItem(registries))) {
                return true
            }
        }

        return false
    }
}