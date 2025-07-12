package net.typho.nemesis.mixin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import net.typho.nemesis.BalancedEnchantment;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(
            method = "getName",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getName(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir) {
        MutableText text = enchantment.value().description().copy();

        if (enchantment.isIn(EnchantmentTags.CURSE)) {
            Texts.setStyleIfAbsent(text, Style.EMPTY.withColor(Formatting.RED));
        } else {
            Texts.setStyleIfAbsent(text, Style.EMPTY.withColor(Formatting.GRAY));
        }

        if (level != 1 || enchantment.value().getMaxLevel() != 1) {
            text.append(ScreenTexts.SPACE).append(Nemesis.toRomanNumeral(level));
        }

        Color low = new Color(89, 43, 144), high = new Color(167, 85, 255);
        float capacity = Math.max(0, Math.min(1, BalancedEnchantment.cast(enchantment.value().definition()).getCapacity() / 4f));

        text.setStyle(Style.EMPTY.withColor(new Color(
                (int) (low.getRed() * (1 - capacity) + high.getRed() * capacity),
                (int) (low.getGreen() * (1 - capacity) + high.getGreen() * capacity),
                (int) (low.getBlue() * (1 - capacity) + high.getBlue() * capacity)
        ).getRGB()));

        cir.setReturnValue(text);
    }

    @Inject(
            method = "isAcceptableItem",
            at = @At("RETURN"),
            cancellable = true
    )
    private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (!Nemesis.canFitEnchantment(stack, (Enchantment) (Object) this)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Mixin(Enchantment.Definition.class)
    @Implements(@Interface(iface = BalancedEnchantment.class, prefix = "bal$"))
    public abstract static class DefinitionMixin {
        @Final
        @Mutable
        @Shadow
        public static MapCodec<Enchantment.Definition> CODEC;

        static {
            MapCodec<Enchantment.Definition> vanilla = CODEC;

            CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    vanilla.forGetter(def -> def),
                    RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("catalyst", RegistryEntry.of(null)).forGetter(def -> BalancedEnchantment.cast(def).getCatalyst()),
                    Codecs.rangedInt(0, 16).optionalFieldOf("catalystCount", 0).forGetter(def -> BalancedEnchantment.cast(def).getCatalystCount()),
                    Codecs.rangedInt(0, 4).optionalFieldOf("capacity", 1).forGetter(def -> BalancedEnchantment.cast(def).getCapacity())
            ).apply(instance,
                    (def, catalyst, catalystCount, capacity) -> {
                        BalancedEnchantment.cast(def).setCatalyst(catalyst);
                        BalancedEnchantment.cast(def).setCatalystCount(catalystCount);
                        BalancedEnchantment.cast(def).setCapacity(capacity);
                        return def;
                    }
            ));
        }

        @Unique
        public RegistryEntry<Item> catalyst;

        @Unique
        public int catalystCount = 0, capacity = 1;

        public RegistryEntry<Item> bal$getCatalyst() {
            if (catalyst == null || catalyst.value() == null) {
                return catalyst = Registries.ITEM.getEntry(Items.AMETHYST_SHARD);
            }

            return catalyst;
        }

        public void bal$setCatalyst(RegistryEntry<Item> catalyst) {
            this.catalyst = catalyst;
        }

        public int bal$getCatalystCount() {
            return catalystCount;
        }

        public void bal$setCatalystCount(int catalystCount) {
            this.catalystCount = catalystCount;
        }

        public int bal$getCapacity() {
            return capacity;
        }

        public void bal$setCapacity(int capacity) {
            this.capacity = capacity;
        }
    }
}
