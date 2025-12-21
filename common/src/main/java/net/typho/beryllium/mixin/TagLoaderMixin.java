package net.typho.beryllium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagLoader;
import net.typho.beryllium.Beryllium;
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
                        Beryllium.INSTANCE.getRemovedItems().stream()
                                .noneMatch(item -> BuiltInRegistries.ITEM
                                        .getKey(item)
                                        .equals(((TagEntryAccessor) entry).beryllium$getId())
                                )
                )
                .toList();
    }
}
