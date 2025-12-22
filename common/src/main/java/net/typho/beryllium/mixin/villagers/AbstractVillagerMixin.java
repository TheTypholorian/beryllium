package net.typho.beryllium.mixin.villagers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.typho.beryllium.villagers.HasDailyTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {
    @WrapOperation(
            method = "addOffersFromItemListings",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
            )
    )
    private int addOffersFromItemListings(RandomSource instance, int i, Operation<Integer> original) {
        return original.call(this instanceof HasDailyTrades trades ? trades.beryllium$getTradeRandom() : instance, i);
    }

    @ModifyArg(
            method = "addOffersFromItemListings",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;getOffer(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/trading/MerchantOffer;"
            )
    )
    private RandomSource addOffersFromItemListings(RandomSource instance) {
        return this instanceof HasDailyTrades trades ? trades.beryllium$getTradeRandom() : instance;
    }
}
