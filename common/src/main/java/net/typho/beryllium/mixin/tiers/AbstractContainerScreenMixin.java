package net.typho.beryllium.mixin.tiers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.typho.beryllium.ModItemComponents;
import net.typho.beryllium.tiers.Tier;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {
    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @WrapOperation(
            method = "renderTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V"
            )
    )
    @SuppressWarnings("deprecation")
    private void renderTooltip(
            GuiGraphics graphics,
            Font font,
            List<Component> tooltipLines,
            Optional<TooltipComponent> visualTooltipComponent,
            int mouseX,
            int mouseY,
            Operation<Void> original,
            @Local ItemStack stack
    ) {
        Tier tier = stack.get(ModItemComponents.tier.get());

        if (tier != null) {
            List<ClientTooltipComponent> tooltip = tooltipLines.stream()
                    .map(Component::getVisualOrderText)
                    .map(ClientTooltipComponent::create)
                    .collect(Util.toMutableList());
            visualTooltipComponent.ifPresent(tooltipComponent -> tooltip.add(tooltip.isEmpty() ? 0 : 1, ClientTooltipComponent.create(tooltipComponent)));

            if (!tooltip.isEmpty()) {
                int i = 0;
                int j = tooltip.size() == 1 ? -2 : 0;

                for (ClientTooltipComponent component : tooltip) {
                    int k = component.getWidth(font);
                    if (k > i) {
                        i = k;
                    }

                    j += component.getHeight();
                }

                int l = i;
                int m = j;
                Vector2ic vector2ic = DefaultTooltipPositioner.INSTANCE.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, l, m);
                int n = vector2ic.x();
                int o = vector2ic.y();
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 400.0F);
                graphics.drawManaged(() -> graphics.blitSprite(
                        tier.getTooltipBackground(),
                        n - 4,
                        o - 4,
                        l + 8,
                        m + 8
                ));
                int q = o;

                for (int r = 0; r < tooltip.size(); r++) {
                    ClientTooltipComponent component = tooltip.get(r);
                    component.renderText(font, n, q, graphics.pose().last().pose(), graphics.bufferSource());
                    q += component.getHeight() + (r == 0 ? 2 : 0);
                }

                q = o;

                for (int r = 0; r < tooltip.size(); r++) {
                    ClientTooltipComponent component = tooltip.get(r);
                    component.renderImage(font, n, q, graphics);
                    q += component.getHeight() + (r == 0 ? 2 : 0);
                }

                graphics.pose().popPose();
            }

            return;
        }

        original.call(graphics, font, tooltipLines, visualTooltipComponent, mouseX, mouseY);
    }
}
