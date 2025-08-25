package net.typho.beryllium.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.gen.feature.Feature;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.armor.*;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.enchanting.EnchantmentInfo;

import java.util.List;
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

    public <I extends Item> I item(String id, I item) {
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

    public Block blockWithItem(String id, Block block, Item item) {
        Block res = Registry.register(Registries.BLOCK, id(id), block);
        item(id, item);
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

    public BlockFamilyBuilder blockFamily(String prefix, AbstractBlock copy) {
        return blockFamily(prefix, AbstractBlock.Settings.copy(copy));
    }

    public BlockFamilyBuilder blockFamily(String prefix, AbstractBlock.Settings settings) {
        return new BlockFamilyBuilder(this, prefix, settings);
    }

    public FoodComponent.Builder timedFood(float eatSeconds) {
        FoodComponent.Builder builder = new FoodComponent.Builder();
        builder.eatSeconds = eatSeconds;
        return builder;
    }

    public ArmorTrimPatternEffect trimPatternEffect(RegistryKey<ArmorTrimPattern> pattern, ArmorTrimPatternEffect effect) {
        return Registry.register(Armor.ARMOR_TRIM_PATTERN_EFFECTS, pattern.getValue(), effect);
    }

    public ArmorTrimPatternEffect trimPatternEffect(RegistryKey<ArmorTrimPattern> pattern, RegistryEntry<EntityAttribute> attribute, double d, EntityAttributeModifier.Operation op, CustomTrimEffect... effects) {
        return trimPatternEffect(pattern, new ArmorTrimPatternEffect(
                List.of(new AttributeModifier(
                        attribute,
                        new EntityAttributeModifier(
                                id(pattern.getValue().getPath()),
                                d,
                                op
                        )
                )),
                List.of(effects)
        ));
    }

    public ArmorTrimPatternEffect trimPatternEffect(RegistryKey<ArmorTrimPattern> pattern, CustomTrimEffect... effects) {
        return trimPatternEffect(pattern, new ArmorTrimPatternEffect(
                List.of(),
                List.of(effects)
        ));
    }

    public ArmorTrimMaterialEffect trimMaterialEffect(RegistryKey<ArmorTrimMaterial> pattern, ArmorTrimMaterialEffect effect) {
        return Registry.register(Armor.ARMOR_TRIM_MATERIAL_EFFECTS, pattern.getValue(), effect);
    }

    @SafeVarargs
    public final ArmorTrimMaterialEffect trimMaterialEffect(RegistryKey<ArmorTrimMaterial> pattern, CustomTrimEffect effect, RegistryKey<ArmorMaterial>... debuffed) {
        return trimMaterialEffect(pattern, new ArmorTrimMaterialEffect(
                List.of(),
                List.of(effect),
                List.of(debuffed)
        ));
    }

    @SafeVarargs
    public final ArmorTrimMaterialEffect trimMaterialEffect(RegistryKey<ArmorTrimMaterial> pattern, RegistryEntry<EntityAttribute> attribute, double d, EntityAttributeModifier.Operation op, RegistryKey<ArmorMaterial>... debuffed) {
        return trimMaterialEffect(pattern, new ArmorTrimMaterialEffect(
                List.of(new AttributeModifier(
                        attribute,
                        new EntityAttributeModifier(
                                id(pattern.getValue().getPath()),
                                d,
                                op
                        )
                )),
                List.of(),
                List.of(debuffed)
        ));
    }

    public EnchantmentInfo enchantmentInfo(Identifier id, EnchantmentInfo info) {
        return Registry.register(Enchanting.ENCHANTMENT_INFO, id, info);
    }

    public RegistryEntry<StatusEffect> statusEffect(String id, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, id(id), effect);
    }
}
