package net.typho.nemesis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.typho.nemesis.Nemesis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow
    @Final
    private Property levelCost;
    @Shadow
    private int repairItemUsage;
    @Shadow
    private String newItemName;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(
            method = "updateResult",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateResult(CallbackInfo ci) {
        ItemStack itemStack = this.input.getStack(0);
        this.levelCost.set(1);
        int i = 0;
        int j = 0;
        if (itemStack.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        } else {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = this.input.getStack(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack2);
            j += itemStack.getRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getRepairCost());
            this.repairItemUsage = 0;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(itemStack3).isEmpty();
                if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                    int l = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    if (l <= 0) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        ci.cancel();
                    }

                    int m;
                    for (m = 0; l > 0 && m < itemStack3.getCount(); m++) {
                        int n = itemStack2.getDamage() - l;
                        itemStack2.setDamage(n);
                        i++;
                        l = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    }

                    this.repairItemUsage = m;
                } else {
                    if (!bl && (!itemStack2.isOf(itemStack3.getItem()) || !itemStack2.isDamageable())) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        ci.cancel();
                    }

                    if (itemStack2.isDamageable() && !bl) {
                        int lx = itemStack.getMaxDamage() - itemStack.getDamage();
                        int m = itemStack3.getMaxDamage() - itemStack3.getDamage();
                        int n = m + itemStack2.getMaxDamage() * 12 / 100;
                        int o = lx + n;
                        int p = itemStack2.getMaxDamage() - o;
                        if (p < 0) {
                            p = 0;
                        }

                        if (p < itemStack2.getDamage()) {
                            itemStack2.setDamage(p);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map2 = EnchantmentHelper.get(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;

                    for (Enchantment enchantment : map2.keySet()) {
                        if (enchantment != null) {
                            int q = map.getOrDefault(enchantment, 0);

                            if (q == 0 && map.size() >= Nemesis.MAX_ENCHANTMENTS) {
                                continue;
                            }

                            int r = map2.get(enchantment);
                            r = q == r ? r + 1 : Math.max(r, q);
                            boolean bl4 = enchantment.isAcceptableItem(itemStack);
                            if (this.player.getAbilities().creativeMode || itemStack.isOf(Items.ENCHANTED_BOOK)) {
                                bl4 = true;
                            }

                            for (Enchantment enchantment2 : map.keySet()) {
                                if (enchantment2 != enchantment && !enchantment.canCombine(enchantment2)) {
                                    bl4 = false;
                                    i++;
                                }
                            }

                            if (!bl4) {
                                bl3 = true;
                            } else {
                                bl2 = true;
                                if (r > enchantment.getMaxLevel()) {
                                    r = enchantment.getMaxLevel();
                                }

                                map.put(enchantment, r);
                                int s = switch (enchantment.getRarity()) {
                                    case COMMON -> 1;
                                    case UNCOMMON -> 2;
                                    case RARE -> 4;
                                    case VERY_RARE -> 8;
                                };

                                if (bl) {
                                    s = Math.max(1, s / 2);
                                }

                                i += s * r;
                                if (itemStack.getCount() > 1) {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (bl3 && !bl2) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        ci.cancel();
                    }
                }
            }

            if (this.newItemName != null && !Util.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(itemStack.getName().getString())) {
                    i++;
                    itemStack2.setCustomName(Text.literal(this.newItemName));
                }
            } else if (itemStack.hasCustomName()) {
                i++;
                itemStack2.removeCustomName();
            }

            this.levelCost.set(j + i);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                EnchantmentHelper.set(map, itemStack2);
            }

            this.output.setStack(0, itemStack2);
            this.sendContentUpdates();
        }

        ci.cancel();
    }

    @Inject(
            method = "getNextCost",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getNextCost(int cost, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) (cost * 1.5 + 1));
    }
}
