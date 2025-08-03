package net.typho.beryllium.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.util.Arm;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.GrassColors;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.building.FillingWandItem;
import net.typho.beryllium.building.kiln.KilnScreen;
import net.typho.beryllium.combat.*;
import net.typho.beryllium.config.SyncServerConfigS2C;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.exploring.MetalDetectorItem;
import net.typho.beryllium.redstone.GoldHopperScreen;
import net.typho.beryllium.redstone.Redstone;
import org.joml.Vector2i;

import java.awt.*;
import java.util.Objects;

public class BerylliumClient implements ClientModInitializer {
    public static final ClientConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

    //public static final Registry<DynamicSpriteLoader> DYNAMIC_TEXTURES = FabricRegistryBuilder.<DynamicSpriteLoader>createSimple(RegistryKey.ofRegistry(id("dynamic_textures"))).buildAndRegister();

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Combat.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(DiamondArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/diamond_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.IRON_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(IronArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/iron_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.FLAMING_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(FlamingArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/flaming_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.COPPER_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(CopperArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/copper_arrow.png");
            }
        });
        EntityRendererRegistry.register(Combat.END_CRYSTAL_PROJECTILE_ENTITY, EndCrystalProjectileEntityRenderer::new);
        ModelPredicateProviderRegistry.register(Exploring.METAL_DETECTOR_ITEM, Identifier.ofVanilla("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> MetalDetectorItem.nearestOre(entity, world)));
        ParticleFactoryRegistry.getInstance().register(
                Exploring.FIREFLY_PARTICLE,
                FireflyFactory::new
        );
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                DyeColor color = stack.get(Exploring.COMPASS_NEEDLE_COMPONENT);

                if (color == null) {
                    color = DyeColor.RED;
                }

                return color.getSignColor() | 0xFF000000;
            }

            return -1;
        }, Items.COMPASS);
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                float hue = (System.currentTimeMillis() % 10000) / 5000f - 1;
                float sat = (System.currentTimeMillis() % 7000) / 3500f - 1;

                hue = hue * hue;
                sat = sat * sat;

                return Color.HSBtoRGB(Math.abs(hue), sat / 4 + 0.5f, 1);
            }

            return -1;
        }, Exploring.EXODINE_INGOT);
        ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> {
            if (index != 0) {
                return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
            } else {
                return -1;
            }
        }, Exploring.DAFFODILS, Exploring.SCILLA, Exploring.GERANIUMS);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> view != null && view.getBlockEntityRenderData(pos) instanceof Integer color ? color : -1, Combat.POTION_CAULDRON);
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.FIREFLY_BOTTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.ALGAE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.DAFFODILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.SCILLA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.GERANIUMS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.VOID_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.POINTED_BONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Exploring.CONGEALED_VOID, RenderLayer.getTranslucent());
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hit) -> {
            PlayerEntity player = MinecraftClient.getInstance().player;

            if (player != null) {
                ItemStack held = player.getMainHandStack();

                if (held.getItem() instanceof FillingWandItem && hit instanceof BlockHitResult blockHit) {
                    MatrixStack matrices = Objects.requireNonNull(context.matrixStack());
                    Vec3d cam = context.camera().getPos();

                    matrices.push();
                    matrices.translate(-cam.x, -cam.y, -cam.z);
                    RenderSystem.disableDepthTest();

                    BlockBox box = FillingWandItem.getSelection(player, held, blockHit);

                    WorldRenderer.drawBox(
                            matrices,
                            Objects.requireNonNull(context.consumers()).getBuffer(RenderLayer.getLines()),
                            box.getMinX(), box.getMinY(), box.getMinZ(),
                            box.getMaxX() + 1, box.getMaxY() + 1, box.getMaxZ() + 1,
                            1, 1, 1, 1,
                            0.5f, 0.5f, 0.5f
                    );

                    RenderSystem.enableDepthTest();
                    matrices.pop();

                    return false;
                }
            }

            return true;
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncServerConfigS2C.ID, (payload, context) -> Beryllium.SERVER_CONFIG = payload.config());
        HandledScreens.register(Redstone.GOLD_HOPPER_SCREEN_HANDLER_TYPE, GoldHopperScreen::new);
        HandledScreens.register(Building.KILN_SCREEN_HANDLER_TYPE, KilnScreen::new);
        /*
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            RenderSystem.recordRenderCall(() -> {
                ShaderProgram shader = RenderSystem.getShader();

                if (shader != null) {
                    int loc = GlUniform.getUniformLocation(shader.getGlRef(), "uUltraDark");

                    if (loc >= 0) {
                        GlUniform uniform = new GlUniform("uUltraDark", 0, 1, shader);
                        shader.bind();
                        uniform.set(1);
                        uniform.upload();
                        //System.out.println("binding loc " + loc);
                    }

                    GlUniform uniform = shader.getUniform("uUltraDark");

                    if (uniform != null) {
                        uniform.set(1);
                    }
                }
            });
        });
         */
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

    public static class ClientConfig extends Config {
        public boolean hudItemTooltips = true;
        public boolean compassCoords = true;

        public ClientConfig() {
            super(Identifier.of(Beryllium.MOD_ID, "client"));
        }
    }
}
