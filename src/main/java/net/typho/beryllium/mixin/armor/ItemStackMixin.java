package net.typho.beryllium.mixin.armor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.armor.Armor;
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

    @Inject(
            method = "appendAttributeModifiersTooltip",
            at = @At("TAIL")
    )
    private void appendAttributeModifiersTooltip(Consumer<Text> textConsumer, @Nullable PlayerEntity player, CallbackInfo ci) {
        if (getItem() instanceof SmithingTemplateItem template) {
            Identifier key = Armor.SMITHING_TEMPLATE_PATTERNS.get(template);

            if (key != null) {
                ArmorTrimPatternEffect effect = Armor.ARMOR_TRIM_PATTERN_EFFECTS.get(key);

                if (effect != null) {
                    effect.appendTooltip(textConsumer, player, (ItemStack) (Object) this);
                }
            }
        }
    }
}
