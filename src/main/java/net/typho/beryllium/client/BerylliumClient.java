package net.typho.beryllium.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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
import net.typho.beryllium.building.MagicWandItem;
import net.typho.beryllium.combat.CopperArrowEntity;
import net.typho.beryllium.combat.DiamondArrowEntity;
import net.typho.beryllium.combat.FlamingArrowEntity;
import net.typho.beryllium.combat.IronArrowEntity;
import net.typho.beryllium.exploring.MetalDetectorItem;
import org.joml.Vector2i;

import java.awt.*;
import java.util.Objects;

public class BerylliumClient implements ClientModInitializer {
    //public static final Registry<DynamicSpriteLoader> DYNAMIC_TEXTURES = FabricRegistryBuilder.<DynamicSpriteLoader>createSimple(RegistryKey.ofRegistry(id("dynamic_textures"))).buildAndRegister();

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Beryllium.COMBAT.DIAMOND_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(DiamondArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/diamond_arrow.png");
            }
        });
        EntityRendererRegistry.register(Beryllium.COMBAT.IRON_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(IronArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/iron_arrow.png");
            }
        });
        EntityRendererRegistry.register(Beryllium.COMBAT.FLAMING_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(FlamingArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/flaming_arrow.png");
            }
        });
        EntityRendererRegistry.register(Beryllium.COMBAT.COPPER_ARROW_TYPE, ctx -> new ProjectileEntityRenderer<>(ctx) {
            @Override
            public Identifier getTexture(CopperArrowEntity entity) {
                return Identifier.of(Beryllium.MOD_ID, "textures/entity/projectiles/combat/copper_arrow.png");
            }
        });
        EntityRendererRegistry.register(Beryllium.COMBAT.END_CRYSTAL_PROJECTILE_ENTITY, EndCrystalProjectileEntityRenderer::new);
        ModelPredicateProviderRegistry.register(Beryllium.EXPLORING.METAL_DETECTOR_ITEM, Identifier.ofVanilla("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> MetalDetectorItem.nearestOre(entity, world)));
        ParticleFactoryRegistry.getInstance().register(
                Beryllium.EXPLORING.FIREFLY_PARTICLE,
                FireflyFactory::new
        );
        ParticleFactoryRegistry.getInstance().register(
                Beryllium.EXPLORING.BIRCH_LEAVES_PARTICLE,
                spriteProvider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new LeavesParticle(world, x, y, z, spriteProvider)
        );
        ParticleFactoryRegistry.getInstance().register(
                Beryllium.EXPLORING.SPRUCE_LEAVES_PARTICLE,
                spriteProvider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new LeavesParticle(world, x, y, z, spriteProvider)
        );
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                DyeColor color = stack.get(Beryllium.EXPLORING.COMPASS_NEEDLE_COMPONENT);

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
        }, Beryllium.EXPLORING.EXODINE_INGOT);
        BlockColorProvider grassTintColorProvider = (state, world, pos, index) -> {
            if (index != 0) {
                return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
            } else {
                return -1;
            }
        };
        ColorProviderRegistry.BLOCK.register(grassTintColorProvider, Beryllium.EXPLORING.DAFFODILS);
        ColorProviderRegistry.BLOCK.register(grassTintColorProvider, Beryllium.EXPLORING.SCILLA);
        BlockRenderLayerMap.INSTANCE.putBlock(Beryllium.EXPLORING.FIREFLY_BOTTLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Beryllium.EXPLORING.ALGAE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Beryllium.EXPLORING.DAFFODILS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Beryllium.EXPLORING.SCILLA, RenderLayer.getCutout());
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hit) -> {
            PlayerEntity player = MinecraftClient.getInstance().player;

            if (player != null) {
                ItemStack held = player.getMainHandStack();

                if (held.getItem() instanceof MagicWandItem && hit instanceof BlockHitResult blockHit) {
                    MatrixStack matrices = Objects.requireNonNull(context.matrixStack());
                    Vec3d cam = context.camera().getPos();

                    matrices.push();
                    matrices.translate(-cam.x, -cam.y, -cam.z);
                    RenderSystem.disableDepthTest();

                    BlockBox box = MagicWandItem.getSelection(player, held, blockHit);

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
        /*
        try {
            File dir = new File("C:/Users/Evan/IdeaProjects/beryllium/compass/");
            for (File imgFile : Objects.requireNonNull(dir.listFiles((d, n) -> n.endsWith(".png")))) {
                BufferedImage img = ImageIO.read(imgFile);
                BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                for (int x = 0; x < img.getWidth(); x++) {
                    for (int y = 0; y < img.getHeight(); y++) {
                        int src = img.getRGB(x, y);

                        if (src != 0) {
                            int red = (src & 0xFF0000) >> 16;
                            int rgb = new Color(red, red, red).getRGB();
                            out.setRGB(x, y, rgb);
                        }
                    }
                }
                File outFile = new File(imgFile.getParentFile().toString() + "/compass/" + imgFile.getName().substring("compass_".length()));
                Files.createDirectories(outFile.toPath());
                ImageIO.write(out, "PNG", outFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        /*
                                    color = switch (color) {
                                case 0xFFFFFFFF -> 0xFFB6C3FB;
                                case 0xFFD8D8D8 -> 0xFF8299FC;
                                case 0xFFA8A8A8 -> 0xFF567CE7;
                                case 0xFF828282 -> 0xFF365AC1;
                                case 0xFF5E5E5E, 0xFF646464 -> 0xFF314E9C;
                                case 0xFF353535 -> 0xFF29418A;
                                case 0xFF2F2F2F, 0xFF171718, 0xFF4D4D4F -> 0xFF21346D;
                                default -> color;
                            };
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
}
