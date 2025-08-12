package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.armor.Armor;
import net.typho.beryllium.armor.ArmorTrimMaterialEffect;
import net.typho.beryllium.armor.ArmorTrimPatternEffect;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isIn(TagKey<Item> tag);

    @Inject(
            method = "appendAttributeModifiersTooltip",
            at = @At("TAIL")
    )
    private void appendAttributeModifiersTooltip(Consumer<Text> textConsumer, @Nullable PlayerEntity player, CallbackInfo ci) {
        if (getItem() instanceof SmithingTemplateItem template) {
            Identifier key = Armor.SMITHING_TEMPLATE_PATTERNS.get(template);

            if (key != null) {
                ArmorTrimPatternEffect pattern = Armor.ARMOR_TRIM_PATTERN_EFFECTS.get(key);

                if (pattern != null) {
                    pattern.appendTooltip(textConsumer, player, (ItemStack) (Object) this);
                }
            }
        } else if (isIn(ItemTags.TRIM_MATERIALS)) {
            ArmorTrimMaterialEffect material = Armor.ARMOR_TRIM_MATERIAL_EFFECTS.get(Registries.ITEM.getId(getItem()));

            if (material != null) {
                material.appendTooltip(textConsumer, player, (ItemStack) (Object) this);
            }
        }
    }
}
