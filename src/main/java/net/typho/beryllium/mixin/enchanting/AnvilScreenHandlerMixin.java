package net.typho.beryllium.mixin.enchanting;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.enchanting.HasEnchantmentInfo;
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
    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private @Nullable String newItemName;

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
        int cost = 0;

        if (!input0.isEmpty() && !input1.isEmpty()) {
            if (input0.getItem() == input1.getItem() || input1.getItem() == Items.ENCHANTED_BOOK) {
                if (EnchantmentHelper.canHaveEnchantments(input0)) {
                    if (EnchantmentHelper.hasEnchantments(input1)) {
                        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
                        output = input0.copy();

                        EnchantmentHelper.set(output, builder.build());

                        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(input0).getEnchantmentEntries()) {
                            add(builder, entry.getKey(), entry.getIntValue(), output);

                            EnchantmentHelper.set(output, builder.build());
                        }

                        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(input1).getEnchantmentEntries()) {
                            cost += add(builder, entry.getKey(), entry.getIntValue(), output);

                            EnchantmentHelper.set(output, builder.build());
                        }

                        if (builder.getEnchantments().equals(EnchantmentHelper.getEnchantments(input0).getEnchantments())) {
                            cost = 0;
                            output = ItemStack.EMPTY;
                        }
                    }
                }
            }
        }

        if (newItemName != null && !StringHelper.isBlank(newItemName)) {
            if (!newItemName.equals(input0.getName().getString())) {
                if (!input0.isEmpty() && (input1.isEmpty() || !output.isEmpty())) {
                    if (output.isEmpty()) {
                        output = input0.copy();
                    }

                    output.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
                }
            }
        }

        levelCost.set(cost);
        this.output.setStack(0, output);
        ci.cancel();
    }

    @Unique
    private static int add(ItemEnchantmentsComponent.Builder builder, RegistryEntry<Enchantment> enchant, int level, ItemStack output) {
        int cost = 0;

        if (enchant.value().isAcceptableItem(output)) {
            int srcLevel = EnchantmentHelper.getLevel(enchant, output);

            if (srcLevel == level) {
                if (level != enchant.value().getMaxLevel() + Enchanting.getExtraLevels(output)) {
                    cost += level++;
                }
            } else if (srcLevel > level) {
                level = srcLevel;
            } else {
                cost += level;
            }

            cost += ((HasEnchantmentInfo) (Object) enchant.value()).getInfo().size();

            builder.add(enchant, level);
        }

        return cost;
    }
}
