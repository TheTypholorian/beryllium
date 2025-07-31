package net.typho.beryllium.mixin.exploring;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable, Merchant {
    @Shadow
    public abstract void setTarget(@Nullable LivingEntity target);

    @Unique
    private TradeOfferList offers;
    @Unique
    private PlayerEntity customer;

    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        setCustomer(player);
        return ActionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        if (this.customer != null && customer == null) {
            if (offers != null) {
                boolean traded = false;

                for (TradeOffer offer : offers) {
                    if (offer.hasBeenUsed()) {
                        traded = true;
                        break;
                    }
                }

                if (!traded) {
                    setTarget(this.customer);
                }
            }
        }

        this.customer = customer;

        if (customer != null) {
            sendOffers(customer, getDisplayName(), 0);
        }
    }

    @Override
    public @Nullable PlayerEntity getCustomer() {
        return customer;
    }

    @Override
    public TradeOfferList getOffers() {
        if (offers == null) {
            offers = new TradeOfferList();
            fillRecipes();
        }

        return offers;
    }

    @Unique
    private void fillRecipes() {
        TradeOffers.Factory[] common = Beryllium.EXPLORING.ENDERMAN_TRADES.get(1);
        TradeOffers.Factory[] rare = Beryllium.EXPLORING.ENDERMAN_TRADES.get(2);

        if (common != null && rare != null) {
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(tradeOfferList, common, 5);
            int i = this.random.nextInt(rare.length);
            TradeOffers.Factory factory = rare[i];
            TradeOffer tradeOffer = factory.create(this, this.random);

            if (tradeOffer != null) {
                tradeOfferList.add(tradeOffer);
            }
        }
    }

    @Unique
    private void fillRecipesFromPool(TradeOfferList recipeList, TradeOffers.Factory[] pool, int count) {
        ArrayList<TradeOffers.Factory> arrayList = Lists.newArrayList(pool);
        int i = 0;

        while (i < count && !arrayList.isEmpty()) {
            TradeOffer tradeOffer = arrayList.remove(this.random.nextInt(arrayList.size())).create(this, this.random);

            if (tradeOffer != null) {
                recipeList.add(tradeOffer);
                i++;
            }
        }
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            TradeOfferList tradeOfferList = this.getOffers();
            if (!tradeOfferList.isEmpty()) {
                nbt.put("Offers", TradeOfferList.CODEC.encodeStart(this.getRegistryManager().getOps(NbtOps.INSTANCE), tradeOfferList).getOrThrow());
            }
        }
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Offers")) {
            TradeOfferList.CODEC
                    .parse(this.getRegistryManager().getOps(NbtOps.INSTANCE), nbt.get("Offers"))
                    .resultOrPartial(Util.addPrefix("Failed to load offers: ", System.err::println))
                    .ifPresent(offers -> this.offers = offers);
        }
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
    }

    @Override
    public void onSellingItem(ItemStack stack) {
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    @Override
    public boolean isClient() {
        return getWorld().isClient;
    }
}
