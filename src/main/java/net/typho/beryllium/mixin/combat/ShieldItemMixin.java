package net.typho.beryllium.mixin.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.config.Config;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {
    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack.getItem(), Config.shieldLowerCooldown.get());
        }

        if (Config.shieldDurability.get()) {
            stack.set(Combat.SHIELD_DURABILITY, (float) Config.shieldMaxDurability.get());
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (!Config.shieldDurability.get()) {
            return super.isItemBarVisible(stack);
        }

        float d = Combat.shieldDurability(stack);

        if (!Config.durabilityRemoval.get() && d >= Config.shieldMaxDurability.get()) {
            return super.isItemBarVisible(stack);
        }

        return d < Config.shieldMaxDurability.get();
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (!Config.shieldDurability.get()) {
            return super.getItemBarColor(stack);
        }

        float d = Combat.shieldDurability(stack);

        if (!Config.durabilityRemoval.get() && d >= Config.shieldMaxDurability.get()) {
            return super.getItemBarColor(stack);
        }

        return MathHelper.hsvToRgb(Math.max(0, d / (float) Config.shieldMaxDurability.get()) / 3, 1, 1);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (!Config.shieldDurability.get()) {
            return super.getItemBarStep(stack);
        }

        float durability = Config.shieldMaxDurability.get() - stack.getComponents().getOrDefault(Combat.SHIELD_DURABILITY, (float) Config.shieldMaxDurability.get());

        if (!Config.durabilityRemoval.get() && durability <= 0) {
            return super.getItemBarColor(stack);
        }

        return MathHelper.clamp(Math.round(13f - durability * 13f / Config.shieldMaxDurability.get()), 0, 13);
    }
}
