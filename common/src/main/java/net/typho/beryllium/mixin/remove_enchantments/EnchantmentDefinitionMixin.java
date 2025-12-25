package net.typho.beryllium.mixin.remove_enchantments;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.typho.beryllium.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(Enchantment.EnchantmentDefinition.class)
public class EnchantmentDefinitionMixin {
    @ModifyVariable(
            method = "<init>",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static HolderSet<Item> init(HolderSet<Item> value) {
        return ModConfig.instance.enchantmentsEnabled ? value : HolderSet.empty();
    }

    @ModifyVariable(
            method = "<init>",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static Optional<HolderSet<Item>> init(Optional<HolderSet<Item>> value) {
        return ModConfig.instance.enchantmentsEnabled ? value : Optional.empty();
    }
}
