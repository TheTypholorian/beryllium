package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.config.BerylliumConfig;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {
    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack.getItem(), BerylliumConfig.SHIELD_LOWER_COOLDOWN.get());
        }

        if (BerylliumConfig.DURABILITY_REMOVAL.get()) {
            stack.set(Combat.SHIELD_DURABILITY, (float) BerylliumConfig.SHIELD_MAX_DURABILITY.get());
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (!BerylliumConfig.DURABILITY_REMOVAL.get()) {
            return super.isItemBarVisible(stack);
        }

        float d = Combat.shieldDurability(stack);

        if (!BerylliumConfig.DURABILITY_REMOVAL.get() && d >= BerylliumConfig.SHIELD_MAX_DURABILITY.get()) {
            return super.isItemBarVisible(stack);
        }

        return d < BerylliumConfig.SHIELD_MAX_DURABILITY.get();
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (!BerylliumConfig.DURABILITY_REMOVAL.get()) {
            return super.getItemBarColor(stack);
        }

        float d = Combat.shieldDurability(stack);

        if (!BerylliumConfig.DURABILITY_REMOVAL.get() && d >= BerylliumConfig.SHIELD_MAX_DURABILITY.get()) {
            return super.getItemBarColor(stack);
        }

        return MathHelper.hsvToRgb(Math.max(0, d / (float) BerylliumConfig.SHIELD_MAX_DURABILITY.get()) / 3, 1, 1);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (!BerylliumConfig.DURABILITY_REMOVAL.get()) {
            return super.getItemBarStep(stack);
        }

        float durability = BerylliumConfig.SHIELD_MAX_DURABILITY.get() - stack.getComponents().getOrDefault(Combat.SHIELD_DURABILITY, (float) BerylliumConfig.SHIELD_MAX_DURABILITY.get());

        if (!BerylliumConfig.DURABILITY_REMOVAL.get() && durability <= 0) {
            return super.getItemBarColor(stack);
        }

        return MathHelper.clamp(Math.round(13f - durability * 13f / BerylliumConfig.SHIELD_MAX_DURABILITY.get()), 0, 13);
    }
}
