package net.typho.nemesis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract NbtCompound getOrCreateNbt();

    @Shadow
    public abstract int getMaxDamage();

    @Inject(
            method = "addEnchantment",
            at = @At("HEAD"),
            cancellable = true
    )
    private void addEnchantment(Enchantment enchantment, int level, CallbackInfo ci) {
        NbtCompound nbt = getOrCreateNbt();

        if (!nbt.contains("Enchantments", NbtElement.LIST_TYPE)) {
            nbt.put("Enchantments", new NbtList());
        }

        NbtList list = nbt.getList("Enchantments", NbtElement.COMPOUND_TYPE);

        if (list.size() < Nemesis.MAX_ENCHANTMENTS) {
            list.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), (byte)level));
        }

        ci.cancel();
    }

    @Inject(
            method = "isDamageable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(
            method = "getDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getDamage(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(getMaxDamage());
    }

    @Inject(
            method = "setDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void setDamage(int damage, CallbackInfo ci) {
        ci.cancel();
    }
}
