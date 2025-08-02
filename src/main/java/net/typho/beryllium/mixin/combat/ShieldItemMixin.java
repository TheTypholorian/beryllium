package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.Combat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {
    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack.getItem(), Beryllium.CONFIG.combat.shieldLowerCooldown);
        }

        if (Beryllium.CONFIG.combat.shieldDurability) {
            stack.set(Combat.SHIELD_DURABILITY, (float) Beryllium.CONFIG.combat.shieldMaxDurability);
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (!Beryllium.CONFIG.combat.shieldDurability) {
            return super.isItemBarVisible(stack);
        }

        float d = Combat.shieldDurability(stack);

        if (!Beryllium.SERVER_CONFIG.durabilityRemoval.get() && d >= Beryllium.CONFIG.combat.shieldMaxDurability) {
            return super.isItemBarVisible(stack);
        }

        return d < Beryllium.CONFIG.combat.shieldMaxDurability;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (!Beryllium.CONFIG.combat.shieldDurability) {
            return super.getItemBarColor(stack);
        }

        float d = Combat.shieldDurability(stack);

        if (!Beryllium.SERVER_CONFIG.durabilityRemoval.get() && d >= Beryllium.CONFIG.combat.shieldMaxDurability) {
            return super.getItemBarColor(stack);
        }

        return MathHelper.hsvToRgb(Math.max(0, d / (float) Beryllium.CONFIG.combat.shieldMaxDurability) / 3, 1, 1);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (!Beryllium.CONFIG.combat.shieldDurability) {
            return super.getItemBarStep(stack);
        }

        float durability = Beryllium.CONFIG.combat.shieldMaxDurability - stack.getComponents().getOrDefault(Combat.SHIELD_DURABILITY, (float) Beryllium.CONFIG.combat.shieldMaxDurability);

        if (!Beryllium.SERVER_CONFIG.durabilityRemoval.get() && durability <= 0) {
            return super.getItemBarColor(stack);
        }

        return MathHelper.clamp(Math.round(13f - durability * 13f / Beryllium.CONFIG.combat.shieldMaxDurability), 0, 13);
    }
}
