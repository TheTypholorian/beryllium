package net.typho.beryllium.mixin.remove_items.fabric;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.typho.beryllium.RemovedThings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @ModifyArg(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresentOrElse(Ljava/util/function/Consumer;Ljava/lang/Runnable;)V"
            )
    )
    private <T extends WithConditions<Recipe<?>>> Consumer<? super T> apply(Consumer<? super T> action) {
        return r -> {
            if (!RemovedThings.getItems().contains(r.carrier().getResultItem(registries).getItem())) {
                action.accept(r);
            }
        };
    }
}
