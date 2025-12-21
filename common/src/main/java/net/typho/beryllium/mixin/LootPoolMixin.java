package net.typho.beryllium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(LootPool.class)
public class LootPoolMixin {
    @WrapOperation(
            method = "addRandomItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntry;createItemStack(Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)V"
            )
    )
    private void init(
            LootPoolEntry instance,
            Consumer<ItemStack> out,
            LootContext lootContext,
            Operation<Void> original
    ) {
        original.call(instance, (Consumer<ItemStack>) stack -> {
            if (!Beryllium.INSTANCE.getRemovedItems().contains(stack.getItem())) {
                out.accept(stack);
            }
        }, lootContext);
    }
}
