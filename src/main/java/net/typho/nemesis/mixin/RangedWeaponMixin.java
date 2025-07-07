package net.typho.nemesis.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponMixin {
    @Shadow
    @Mutable
    @Final
    public static Predicate<ItemStack> CROSSBOW_HELD_PROJECTILES;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        System.out.println(CROSSBOW_HELD_PROJECTILES);
        CROSSBOW_HELD_PROJECTILES = CROSSBOW_HELD_PROJECTILES.or(stack -> stack.isOf(Items.END_CRYSTAL));
    }
}
