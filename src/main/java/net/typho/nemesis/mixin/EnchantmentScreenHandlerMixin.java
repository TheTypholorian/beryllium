package net.typho.nemesis.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.typho.nemesis.Nemesis;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
    @Shadow
    @Final
    private Inventory inventory;

    @Shadow @Final public int[] enchantmentPower;

    @Shadow @Final private ScreenHandlerContext context;

    @Shadow @Final private Property seed;

    @Shadow @Final public int[] enchantmentId;

    @Shadow @Final public int[] enchantmentLevel;

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(intValue = 15, ordinal = 0)
    )
    private int equipmentSlot(int constant) {
        return 5;
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(intValue = 35, ordinal = 0)
    )
    private int lapisSlot(int constant) {
        return 23;
    }

    @Inject(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/EnchantmentScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void recipeSlot(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        addSlot(new Slot(inventory, 2, 41, 47) {
            @Override
            public @NotNull Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of("item/empty_slot_amethyst_shard"));
            }
        });
    }

    @ModifyConstant(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            constant = @Constant(
                    intValue = 2,
                    ordinal = 0
            )
    )
    private int changeInventorySize(int original) {
        return original + 1;
    }

    @Inject(
            method = "quickMove",
            at = @At("HEAD"),
            cancellable = true
    )
    public void quickMove(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);

        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0 || slot == 1 || slot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            } else if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                if (!this.insertItem(itemStack2, 1, 2, true)) {
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            } else {
                if (this.slots.get(0).hasStack() || !this.slots.get(0).canInsert(itemStack2)) {
                    if (this.slots.get(2).hasStack() || !this.slots.get(2).canInsert(itemStack2)) {
                        cir.setReturnValue(ItemStack.EMPTY);
                    } else {
                        this.slots.get(2).setStack(itemStack2);
                        slot2.setStack(ItemStack.EMPTY);
                    }
                } else {
                    ItemStack itemStack3 = itemStack2.copyWithCount(1);
                    itemStack2.decrement(1);
                    this.slots.get(0).setStack(itemStack3);
                }
            }

            if (cir.getReturnValue() != null) {
                if (itemStack2.isEmpty()) {
                    slot2.setStack(ItemStack.EMPTY);
                } else {
                    slot2.markDirty();
                }

                if (itemStack2.getCount() == itemStack.getCount()) {
                    cir.setReturnValue(ItemStack.EMPTY);
                } else {
                    slot2.onTakeItem(player, itemStack2);
                }
            }
        }

        if (cir.getReturnValue() != null) {
            cir.setReturnValue(itemStack);
        }
    }

    /**
     * @author The Typhothanian
     * @reason Implement enchantment catalysts to reward exploration
     */
    @Overwrite
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id >= 0 && id < enchantmentPower.length) {
            ItemStack enchantSlot = this.inventory.getStack(0);
            ItemStack lapisSlot = this.inventory.getStack(1);
            ItemStack catalystSlot = this.inventory.getStack(2);
            int i = id + 1;
            if ((lapisSlot.isEmpty() || lapisSlot.getCount() < i) && !player.isInCreativeMode()) {
                return false;
            } else if (this.enchantmentPower[id] <= 0
                    || enchantSlot.isEmpty()
                    || (player.experienceLevel < i || player.experienceLevel < this.enchantmentPower[id]) && !player.getAbilities().creativeMode) {
                return false;
            } else {
                context.run((world, pos) -> {
                    ItemStack itemStack3 = enchantSlot;
                    Optional<RegistryEntry.Reference<Enchantment>> enchOptional = player
                            .getWorld()
                            .getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(enchantmentId[id]);

                    if (enchOptional.isPresent()) {
                        RegistryEntry<Enchantment> enchant = enchOptional.get();
                        int level = enchantmentLevel[id];

                        if (Nemesis.hasEnoughCatalysts(catalystSlot, enchant, level, player)) {
                            player.applyEnchantmentCosts(enchantSlot, i);
                            if (enchantSlot.isOf(Items.BOOK)) {
                                itemStack3 = enchantSlot.withItem(Items.ENCHANTED_BOOK);
                                this.inventory.setStack(0, itemStack3);
                            }

                            boolean hasApplied = false;
                            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : EnchantmentHelper.getEnchantments(itemStack3).getEnchantmentEntries()) {
                                if (entry.getKey() == enchant) {
                                    if (level == entry.getIntValue() && level != enchant.value().getMaxLevel()) {
                                        itemStack3.addEnchantment(enchant, level + 1);
                                        hasApplied = true;
                                        break;
                                    }
                                }
                            }

                            if (!hasApplied) {
                                itemStack3.addEnchantment(enchant, level);
                            }

                            lapisSlot.decrementUnlessCreative(i, player);
                            if (lapisSlot.isEmpty()) {
                                this.inventory.setStack(1, ItemStack.EMPTY);
                            }

                            catalystSlot.decrementUnlessCreative(Nemesis.getEnchantmentCatalyst(enchant, level).getCount(), player);
                            if (catalystSlot.isEmpty()) {
                                this.inventory.setStack(2, ItemStack.EMPTY);
                            }

                            player.incrementStat(Stats.ENCHANT_ITEM);
                            if (player instanceof ServerPlayerEntity) {
                                Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemStack3, i);
                            }

                            this.inventory.markDirty();
                            seed.set(player.getEnchantmentTableSeed());
                            this.onContentChanged(this.inventory);
                            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                        }
                    }
                });
                return true;
            }
        } else {
            Util.error(player.getName() + " pressed invalid button id: " + id);
            return false;
        }
    }

    @Redirect(
            method = "onContentChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isEnchantable()Z"
            )
    )
    public boolean isEnchantableRedirect(ItemStack stack) {
        return stack.getItem().isEnchantable(stack);
    }

    @Mixin(targets = "net.minecraft.screen.EnchantmentScreenHandler$2")
    public abstract static class SubMixin2 extends Slot {
        public SubMixin2(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.getItem().isEnchantable(stack);
        }

        @Override
        public @NotNull Pair<Identifier, Identifier> getBackgroundSprite() {
            return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.ofVanilla("item/empty_slot_sword"));
        }
    }
}
