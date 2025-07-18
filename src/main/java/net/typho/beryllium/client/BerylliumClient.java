package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.*;
import org.joml.Vector2i;

import static net.typho.beryllium.BerylliumModule.id;

public class BerylliumClient implements ClientModInitializer {
    public static final Registry<DynamicSpriteLoader> DYNAMIC_TEXTURES = FabricRegistryBuilder.<DynamicSpriteLoader>createSimple(RegistryKey.ofRegistry(id("dynamic_textures"))).buildAndRegister();

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Combat.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(DiamondArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/diamond_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.IRON_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(IronArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/iron_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.FLAMING_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(FlamingArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/flaming_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.COPPER_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(CopperArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/copper_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.END_CRYSTAL_PROJECTILE_ENTITY, EndCrystalProjectileEntityRenderer::new);
    }

    public static void drawTooltip(DrawContext context, ItemStack stack, Arm arm, int x, int y, PlayerEntity player, TextRenderer renderer) {
        context.drawTooltip(
                renderer,
                stack.getTooltip(Item.TooltipContext.DEFAULT, player, TooltipType.BASIC)
                        .stream()
                        .flatMap(t -> renderer.wrapLines(t, 160).stream())
                        .toList(),
                (screenWidth, screenHeight, x1, y1, width1, height1) -> new Vector2i(
                        player.getMainArm() != arm ? x1 - width1 : x1,
                        y1 - height1 - 8
                ),
                x,
                y
        );
    }
}
