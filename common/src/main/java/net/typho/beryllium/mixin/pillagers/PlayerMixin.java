package net.typho.beryllium.mixin.pillagers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.typho.beryllium.ModItemComponents;
import net.typho.beryllium.pillagers.RavagerShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(
            method = "hurtCurrentlyUsedShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean hurtCurrentlyUsedShield(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || item instanceof ShieldItem;
    }

    @Inject(
            method = "hurtCurrentlyUsedShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getUsedItemHand()Lnet/minecraft/world/InteractionHand;"
            )
    )
    private void hurtCurrentlyUsedShield(float damage, CallbackInfo ci, @Local int amount) {
        if (useItem.getItem() instanceof RavagerShieldItem) {
            int charge = useItem.getOrDefault(ModItemComponents.ravagerShieldCharge.get(), 0);
            useItem.set(ModItemComponents.ravagerShieldCharge.get(), Mth.clamp(charge + amount, 0, RavagerShieldItem.MAX_CHARGE));
        }
    }

    @ModifyArg(
            method = "disableShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"
            )
    )
    private Item disableShield(Item item) {
        return useItem.getItem();
    }

    @Inject(
            method = "jumpFromGround",
            at = @At("HEAD"),
            cancellable = true
    )
    private void jumpFromGround(CallbackInfo ci) {
        ItemStack used = getUseItem();
        int charge = used.getOrDefault(ModItemComponents.ravagerShieldCharge.get(), 0);

        if (charge >= RavagerShieldItem.MAX_CHARGE) {
            used.set(ModItemComponents.ravagerShieldCharge.get(), charge - RavagerShieldItem.MAX_CHARGE);
            setDeltaMovement(getViewVector(0).multiply(1.5, 1.5, 1.5));
            playSound(SoundEvents.RAVAGER_ATTACK);
            ci.cancel();
        }
    }
}
