package net.typho.beryllium.mixin.trims.fabric;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.typho.beryllium.ModConfig;
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
                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;",
                    remap = false
            )
    )
    private <K, V> ImmutableMultimap.Builder<K, V> apply(ImmutableMultimap.Builder<K, V> instance, K key, V value, Operation<ImmutableMultimap.Builder<K, V>> original) {
        if (!ModConfig.instance.smithing.templatesDuplicatable) {
            if (((RecipeHolder<?>) value).value() instanceof ShapedRecipe shaped) {
                if (RemovedThings.isSmithingTemplateRecipe(shaped, registries)) {
                    return null;
                }
            }
        }

        return original.call(instance, key, value);
    }

    @WrapOperation(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;",
                    remap = false
            )
    )
    private <K, V> ImmutableMap.Builder<K, V> apply(ImmutableMap.Builder<K, V> instance, K key, V value, Operation<ImmutableMap.Builder<K, V>> original) {
        if (!ModConfig.instance.smithing.templatesDuplicatable) {
            if (((RecipeHolder<?>) value).value() instanceof ShapedRecipe shaped) {
                if (RemovedThings.isSmithingTemplateRecipe(shaped, registries)) {
                    return null;
                }
            }
        }

        return original.call(instance, key, value);
    }
}
