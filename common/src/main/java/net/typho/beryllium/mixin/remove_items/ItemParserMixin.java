package net.typho.beryllium.mixin.remove_items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.typho.beryllium.RemovedThings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.stream.Stream;

@Mixin(targets = "net.minecraft.commands.arguments.item.ItemParser$State")
public class ItemParserMixin {
    @WrapOperation(
            method = "suggestItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/HolderLookup$RegistryLookup;listElementIds()Ljava/util/stream/Stream;"
            )
    )
    private Stream<ResourceKey<Item>> suggestItem(HolderLookup.RegistryLookup<Item> instance, Operation<Stream<ResourceKey<Item>>> original) {
        return original.call(instance).filter(key ->
                RemovedThings.getItems()
                        .stream()
                        .noneMatch(item -> BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow().equals(key))
        );
    }

    @WrapOperation(
            method = "readItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/HolderLookup$RegistryLookup;get(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;"
            )
    )
    private Optional<Holder.Reference<Item>> readItem(HolderLookup.RegistryLookup<Item> instance, ResourceKey<Item> resourceKey, Operation<Optional<Holder.Reference<Item>>> original) {
        return original.call(instance, resourceKey).filter(reference ->
                !RemovedThings.getItems().contains(reference.value())
        );
    }
}
