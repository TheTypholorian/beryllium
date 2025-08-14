package net.typho.beryllium.mixin.enchanting;

import net.minecraft.enchantment.Enchantment;
import net.typho.beryllium.enchanting.EnchantmentInfo;
import net.typho.beryllium.enchanting.HasEnchantmentInfo;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Enchantment.Builder.class)
@Implements(@Interface(iface = HasEnchantmentInfo.class, prefix = "info$"))
public class EnchantmentBuilderMixin {
    @Unique
    public EnchantmentInfo info = EnchantmentInfo.DEFAULT;

    public EnchantmentInfo info$getInfo() {
        return info;
    }

    public void info$setInfo(EnchantmentInfo info) {
        this.info = info;
    }
}
