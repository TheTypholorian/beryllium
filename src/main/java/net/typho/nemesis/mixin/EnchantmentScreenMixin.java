package net.typho.nemesis.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.nemesis.Nemesis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    @Shadow @Final private static Identifier TEXTURE;

    @Shadow protected abstract void drawBook(DrawContext context, int x, int y, float delta);

    @Shadow @Final private static Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE;

    @Shadow @Final private static Identifier[] LEVEL_DISABLED_TEXTURES;

    @Shadow @Final private static Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE;

    @Shadow @Final private static Identifier ENCHANTMENT_SLOT_TEXTURE;

    @Shadow @Final private static Identifier[] LEVEL_TEXTURES;

    public EnchantmentScreenMixin(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    /**
     * @author The Typhothanian
     * @reason Implement enchantment catalysts to reward exploration
     */
    @Overwrite
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        drawBook(context, x, y, delta);
        EnchantingPhrases.getInstance().setSeed(handler.getSeed());
        int lapis = handler.getLapisCount();
        ItemStack catalystSlot = handler.getSlot(2).getStack();

        for (int id = 0; id < 3; id++) {
            int m = x + 60;
            int n = m + 20;
            int power = handler.enchantmentPower[id];

            if (power == 0) {
                RenderSystem.enableBlend();
                context.drawGuiTexture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, y + 14 + 19 * id, 108, 19);
                RenderSystem.disableBlend();
            } else {
                String powerText = power + "";
                int p = 86 - textRenderer.getWidth(powerText);
                StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(textRenderer, p);
                int q = 6839882;
                Optional<RegistryEntry.Reference<Enchantment>> enchOptional = client
                        .world
                        .getRegistryManager()
                        .get(RegistryKeys.ENCHANTMENT)
                        .getEntry(handler.enchantmentId[id]);

                if (enchOptional.isPresent()) {
                    ItemStack catalystReq = Nemesis.getEnchantmentCatalyst(enchOptional.get(), handler.enchantmentLevel[id]);

                    if ((lapis < id + 1 || client.player.experienceLevel < power) && !client.player.getAbilities().creativeMode || catalystReq.getItem() != catalystSlot.getItem() || catalystSlot.getCount() < catalystReq.getCount()) {
                        RenderSystem.enableBlend();
                        context.drawGuiTexture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, y + 14 + 19 * id, 108, 19);
                        context.drawGuiTexture(LEVEL_DISABLED_TEXTURES[id], m + 1, y + 15 + 19 * id, 16, 16);
                        RenderSystem.disableBlend();
                        context.drawTextWrapped(textRenderer, stringVisitable, n, y + 16 + 19 * id, p, (q & 16711422) >> 1);
                        q = 4226832;
                    } else {
                        int r = mouseX - (x + 60);
                        int s = mouseY - (y + 14 + 19 * id);
                        RenderSystem.enableBlend();
                        if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                            context.drawGuiTexture(ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, y + 14 + 19 * id, 108, 19);
                            q = 16777088;
                        } else {
                            context.drawGuiTexture(ENCHANTMENT_SLOT_TEXTURE, m, y + 14 + 19 * id, 108, 19);
                        }

                        context.drawGuiTexture(LEVEL_TEXTURES[id], m + 1, y + 15 + 19 * id, 16, 16);
                        RenderSystem.disableBlend();
                        context.drawTextWrapped(textRenderer, stringVisitable, n, y + 16 + 19 * id, p, q);
                        q = 8453920;
                    }

                    context.drawTextWithShadow(textRenderer, powerText, n + 86 - textRenderer.getWidth(powerText), y + 16 + 19 * id + 7, q);
                }
            }
        }
    }
}
