package net.typho.beryllium.mixin.enchanting;

import com.google.common.collect.Lists;
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
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.typho.beryllium.enchanting.Enchanting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    @Shadow
    @Final
    private static Identifier TEXTURE;

    @Shadow
    protected abstract void drawBook(DrawContext context, int x, int y, float delta);

    @Shadow
    @Final
    private static Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE;

    @Shadow
    @Final
    private static Identifier[] LEVEL_DISABLED_TEXTURES;

    @Shadow
    @Final
    private static Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE;

    @Shadow
    @Final
    private static Identifier ENCHANTMENT_SLOT_TEXTURE;

    @Shadow
    @Final
    private static Identifier[] LEVEL_TEXTURES;

    public EnchantmentScreenMixin(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    /**
     * @author The Typhothanian
     * @reason Implement enchantment catalysts to reward exploration
     */
    @Overwrite
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        if (client != null && client.player != null) {
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
                            .player
                            .getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(handler.enchantmentId[id]);

                    if (enchOptional.isEmpty() || (lapis < id + 1 || client.player.experienceLevel < power) && !client.player.getAbilities().creativeMode || !Enchanting.hasEnoughCatalysts(catalystSlot, enchOptional.get(), handler.enchantmentLevel[id], client.player)) {
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

    /**
     * @author The Typhothanian
     * @reason Add catalyst to the tooltip
     */
    @Overwrite
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (client != null && client.player != null) {
            drawMouseoverTooltip(context, mouseX, mouseY);
            boolean bl = client.player.getAbilities().creativeMode;
            int i = handler.getLapisCount();

            for (int j = 0; j < 3; j++) {
                int k = handler.enchantmentPower[j];
                Optional<RegistryEntry.Reference<Enchantment>> optional = client
                        .player
                        .getRegistryManager()
                        .get(RegistryKeys.ENCHANTMENT)
                        .getEntry(handler.enchantmentId[j]);
                if (optional.isPresent()) {
                    int l = handler.enchantmentLevel[j];
                    int m = j + 1;
                    if (isPointWithinBounds(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0 && l >= 0) {
                        List<Text> list = Lists.newArrayList();
                        list.add(Text.translatable("container.enchant.clue", Enchantment.getName(optional.get(), l)).formatted(Formatting.WHITE));
                        ItemStack catalyst = Enchanting.getCatalyst(optional.get(), l);
                        if (!bl) {
                            list.add(ScreenTexts.EMPTY);
                            if (client.player.experienceLevel < k) {
                                list.add(Text.translatable("container.enchant.level.requirement", handler.enchantmentPower[j]).formatted(Formatting.RED));
                            } else {
                                MutableText mutableText;
                                if (m == 1) {
                                    mutableText = Text.translatable("container.enchant.lapis.one");
                                } else {
                                    mutableText = Text.translatable("container.enchant.lapis.many", m);
                                }

                                list.add(mutableText.formatted(i >= m ? Formatting.GRAY : Formatting.RED));

                                if (!catalyst.isEmpty()) {
                                    list.add(Text.literal(catalyst.getCount() + "x ").append(catalyst.getName()).formatted(Enchanting.hasEnoughCatalysts(handler.getSlot(2).getStack(), optional.get(), l, client.player) ? Formatting.GRAY : Formatting.RED));
                                }

                                MutableText mutableText3;
                                if (m == 1) {
                                    mutableText3 = Text.translatable("container.enchant.level.one");
                                } else {
                                    mutableText3 = Text.translatable("container.enchant.level.many", m);
                                }

                                list.add(mutableText3.formatted(Formatting.GRAY));
                            }
                        }

                        context.drawTooltip(textRenderer, list, mouseX, mouseY);
                        break;
                    }
                }
            }
        }
    }
}
