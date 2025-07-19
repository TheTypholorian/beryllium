package net.typho.beryllium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Arm;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.combat.*;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.exploring.MetalDetectorItem;
import org.joml.Vector2i;

import static net.typho.beryllium.Module.id;

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
        ModelPredicateProviderRegistry.register(Exploring.METAL_DETECTOR_ITEM, Identifier.ofVanilla("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> MetalDetectorItem.nearestOre(entity, world)));
        ColorProviderRegistry.ITEM.register((stack, index) -> {
            if (index == 1) {
                DyeColor color = stack.get(Exploring.COMPASS_NEEDLE_COMPONENT);

                if (color == null) {
                    color = DyeColor.RED;
                }

                return color.getSignColor() | 0xFF000000;
            }

            return 0xFFFFFFFF;
        }, Items.COMPASS);
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
