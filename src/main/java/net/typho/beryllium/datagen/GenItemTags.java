package net.typho.beryllium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GenItemTags extends FabricTagProvider.ItemTagProvider {
    public GenItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    public GenItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(ItemTags.ARROWS)
                .add(Combat.DIAMOND_ARROW)
                .add(Combat.IRON_ARROW)
                .add(Combat.FLAMING_ARROW)
                .add(Combat.COPPER_ARROW);

        getOrCreateTagBuilder(ItemTags.FLOWERS)
                .add(Exploring.DAFFODILS.asItem())
                .add(Exploring.SCILLA.asItem())
                .add(Exploring.GERANIUMS.asItem());

        getOrCreateTagBuilder(Combat.HORSE_ARMOR)
                .add(Items.LEATHER_HORSE_ARMOR)
                .add(Items.IRON_HORSE_ARMOR)
                .add(Items.GOLDEN_HORSE_ARMOR)
                .add(Items.DIAMOND_HORSE_ARMOR)
                .add(Combat.NETHERITE_HORSE_ARMOR);

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .addTag(Combat.HORSE_ARMOR);
    }
}
