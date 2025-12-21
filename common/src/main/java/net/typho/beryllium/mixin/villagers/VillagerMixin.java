package net.typho.beryllium.mixin.villagers;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.typho.beryllium.villagers.HasDeterministicTrades;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager implements HasDeterministicTrades {
    @Unique
    private RandomSource beryllium$tradeRandom = random;

    public VillagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(
            method = "updateSpecialPrices",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;floor(F)I"
            )
    )
    private float updateSpecialPrices(float value) {
        return Math.min(value, 0);
    }

    @Inject(
            method = "updateTrades",
            at = @At("HEAD")
    )
    private void injectUpdateTrades(CallbackInfo ci) {
        beryllium$tradeRandom = RandomSource.create(uuid.getLeastSignificantBits());
    }

    @Override
    public @NotNull RandomSource beryllium$getTradeRandom() {
        return beryllium$tradeRandom;
    }
}
