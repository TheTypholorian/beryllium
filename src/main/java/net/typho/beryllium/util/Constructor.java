package net.typho.beryllium.util;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.gen.feature.Feature;
import net.typho.beryllium.Beryllium;

import java.util.function.UnaryOperator;

public class Constructor implements Identifierifier {
    public final String name;

    public Constructor() {
        this(null);
    }

    public Constructor(String name) {
        this.name = name;
    }

    @Override
    public Identifier id(String id) {
        return Identifier.of(Beryllium.MOD_ID, name == null ? id : (name + "/" + id));
    }

    public Item item(String id, Item item) {
        return Registry.register(Registries.ITEM, id(id), item);
    }

    public Block block(String id, Block block) {
        return Registry.register(Registries.BLOCK, id(id), block);
    }

    public Block blockWithItem(String id, Block block, Item.Settings settings) {
        Block res = Registry.register(Registries.BLOCK, id(id), block);
        item(id, new BlockItem(res, settings));
        return res;
    }

    public Identifier stat(String id, StatFormatter formatter) {
        Identifier identifier = id(id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    public <T extends Feature<?>> T feature(String id, T feature) {
        return Registry.register(Registries.FEATURE, id(id), feature);
    }

    public <T extends net.minecraft.screen.ScreenHandler> ScreenHandlerType<T> screenHandler(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id(id), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public <T extends BlockEntity> BlockEntityType<T> blockEntity(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id(id), builder.build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id)));
    }

    public <T> ComponentType<T> dataComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id(id), builderOperator.apply(ComponentType.builder()).build());
    }

    public RegistryEntry<EntityAttribute> attribute(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, id(id), attribute);
    }

    public <T extends Entity> EntityType<T> entity(String id, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id(id), type);
    }
}
