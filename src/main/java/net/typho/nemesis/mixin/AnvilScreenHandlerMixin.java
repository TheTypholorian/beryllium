package net.typho.nemesis.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AnvilScreenHandler.class, priority = 1100)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(
            method = "updateResult()V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void updateResult(CallbackInfo ci) {
        ItemStack input0 = input.getStack(0), input1 = input.getStack(1);
        ItemStack output = ItemStack.EMPTY;
        levelCost.set(1);

        if (!input0.isEmpty() && !input1.isEmpty()) {
            if (EnchantmentHelper.canHaveEnchantments(input0)) {
                if (EnchantmentHelper.hasEnchantments(input0) || EnchantmentHelper.hasEnchantments(input1)) {
                    ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
                    output = input0.copy();

                    EnchantmentHelper.set(output, builder.build());

                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(input0).getEnchantmentEntries()) {
                        add(builder, entry.getKey(), entry.getIntValue(), output);

                        EnchantmentHelper.set(output, builder.build());
                    }

                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(input1).getEnchantmentEntries()) {
                        add(builder, entry.getKey(), entry.getIntValue(), output);

                        EnchantmentHelper.set(output, builder.build());
                    }
                }
            }
        }

        this.output.setStack(0, output);
        ci.cancel();
    }

    @Unique
    private static void add(ItemEnchantmentsComponent.Builder builder, RegistryEntry<Enchantment> enchant, int level, ItemStack output) {
        if (enchant.value().isAcceptableItem(output)) {
            int srcLevel = EnchantmentHelper.getLevel(enchant, output);

            if (srcLevel == level && level != enchant.value().getMaxLevel()) {
                level++;
            } else if (srcLevel > level) {
                level = srcLevel;
            }

            builder.add(enchant, level);
        }
    }
}
