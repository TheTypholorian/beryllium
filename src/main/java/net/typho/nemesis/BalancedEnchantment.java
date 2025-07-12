package net.typho.nemesis;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

public interface BalancedEnchantment {
    RegistryEntry<Item> getCatalyst();

    void setCatalyst(RegistryEntry<Item> item);

    int getCatalystCount();

    void setCatalystCount(int count);

    int getCapacity();

    void setCapacity(int capacity);

    static @NotNull BalancedEnchantment cast(@NotNull Enchantment.Definition def) {
        return (BalancedEnchantment) (Object) def;
    }
}
