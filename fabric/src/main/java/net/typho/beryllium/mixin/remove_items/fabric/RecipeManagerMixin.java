package net.typho.beryllium.mixin.remove_items.fabric;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.typho.beryllium.RemovedThings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @WrapOperation(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;",
                    remap = false
            )
    )
    private <K, V> ImmutableMap.Builder<K, V> apply1(ImmutableMap.Builder<K, V> instance, K key, V value, Operation<ImmutableMap.Builder<K, V>> original) {
        if (RemovedThings.items.contains(((RecipeHolder<?>) value).value().getResultItem(registries).getItem())) {
            return null;
        }

        return original.call(instance, key, value);
    }

    @WrapOperation(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;",
                    remap = false
            )
    )
    private <K, V> ImmutableMultimap.Builder<K, V> apply2(ImmutableMultimap.Builder<K, V> instance, K key, V value, Operation<ImmutableMultimap.Builder<K, V>> original) {
        if (RemovedThings.items.contains(((RecipeHolder<?>) value).value().getResultItem(registries).getItem())) {
            return null;
        }

        return original.call(instance, key, value);
    }
}
