package net.typho.beryllium.mixin.remove_items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagLoader;
import net.typho.beryllium.RemovedThings;
import net.typho.beryllium.mixin.accessors.TagEntryAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
    @WrapOperation(
            method = "load",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tags/TagFile;entries()Ljava/util/List;"
            )
    )
    private List<TagEntry> load(TagFile instance, Operation<List<TagEntry>> original) {
        return original.call(instance).stream()
                .filter(entry ->
                        RemovedThings.getItems().stream()
                                .noneMatch(item -> BuiltInRegistries.ITEM
                                        .getKey(item)
                                        .equals(((TagEntryAccessor) entry).beryllium$getId())
                                )
                )
                .toList();
    }
}
