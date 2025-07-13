package net.typho.beryllium.mixin;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;

@Mixin(TradeOffers.class)
public abstract class TradeOffersMixin {
    @Shadow
    private static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> map) {
        return null;
    }

    @Redirect(
            method = "method_16929",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private static Object librarian(HashMap<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> map, Object villager, Object value) {
        if (villager == VillagerProfession.LIBRARIAN) {
            return map.put(
                    (VillagerProfession) villager,
                    copyToFastUtilMap(
                            ImmutableMap.<Integer, TradeOffers.Factory[]>builder()
                                    .put(
                                            1,
                                            new TradeOffers.Factory[]{
                                                    new TradeOffers.BuyItemFactory(Items.PAPER, 24, 16, 2),
                                                    new TradeOffers.SellItemFactory(Blocks.BOOKSHELF, 9, 1, 12, 1)
                                            }
                                    )
                                    .put(
                                            2,
                                            new TradeOffers.Factory[]{
                                                    new TradeOffers.BuyItemFactory(Items.BOOK, 4, 12, 10),
                                                    new TradeOffers.SellItemFactory(Items.LANTERN, 1, 1, 5)
                                            }
                                    )
                                    .put(
                                            3,
                                            new TradeOffers.Factory[]{
                                                    new TradeOffers.BuyItemFactory(Items.INK_SAC, 5, 12, 20),
                                                    new TradeOffers.SellItemFactory(Items.GLASS, 1, 4, 10)
                                            }
                                    )
                                    .put(
                                            4,
                                            new TradeOffers.Factory[]{
                                                    new TradeOffers.BuyItemFactory(Items.WRITABLE_BOOK, 2, 12, 30),
                                                    new TradeOffers.SellItemFactory(Items.CLOCK, 5, 1, 15),
                                                    new TradeOffers.SellItemFactory(Items.COMPASS, 4, 1, 15)
                                            }
                                    )
                                    .put(5, new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(Items.NAME_TAG, 20, 1, 30)})
                                    .build()
                    )
            );
        } else {
            return map.put((VillagerProfession) villager, (Int2ObjectMap<TradeOffers.Factory[]>) value);
        }
    }
}
