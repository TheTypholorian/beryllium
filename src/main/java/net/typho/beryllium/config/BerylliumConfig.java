package net.typho.beryllium.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.exploring.Exploring;
import net.typho.config.ConfigOption;
import net.typho.config.ConfigOptionGroup;
import net.typho.config.RootConfigOptionGroup;
import net.typho.config.SetConfigValuePacket;
import net.typho.config.impl.BooleanConfigOption;
import net.typho.config.impl.RangedFloatConfigOption;
import net.typho.config.impl.RangedIntConfigOption;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BerylliumConfig implements ModInitializer {
    public static final Map<Identifier, ConfigOption<?>> ALL_OPTIONS = new LinkedHashMap<>();

    public static final Set<Identifier> DURABILITY_ENCHANTS = new HashSet<>(Set.of(
            Identifier.ofVanilla("mending"),
            Identifier.ofVanilla("unbreaking")
    ));

    public static final RootConfigOptionGroup ROOT_GROUP = new RootConfigOptionGroup.Builder().icon(new ItemStack(Items.END_CRYSTAL)).id(Beryllium.BASE_CONSTRUCTOR.id("config")).build();

    public static final ConfigOptionGroup ARMOR_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.DIAMOND_CHESTPLATE)).parent(ROOT_GROUP).id("armor").build();
    public static final ConfigOptionGroup BUILDING_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.BRICKS)).parent(ROOT_GROUP).id("building").build();
    public static final ConfigOptionGroup CHALLENGES_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.TRIAL_KEY)).parent(ROOT_GROUP).id("challenges").build();
    public static final ConfigOptionGroup COMBAT_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.DIAMOND_SWORD)).parent(ROOT_GROUP).id("combat").build();
    public static final ConfigOptionGroup ENCHANTING_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.ENCHANTED_BOOK)).parent(ROOT_GROUP).id("enchanting").build();
    public static final ConfigOptionGroup EXPLORING_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.FILLED_MAP)).parent(ROOT_GROUP).id("exploring").build();
    public static final ConfigOptionGroup REDSTONE_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.REDSTONE)).parent(ROOT_GROUP).id("redstone").build();
    public static final ConfigOptionGroup CLIENT_GROUP = new ConfigOptionGroup.Builder().icon(new ItemStack(Items.PAPER)).env(EnvType.CLIENT).parent(ROOT_GROUP).id("client").build();

    public static final BooleanConfigOption DURABILITY_REMOVAL = new BooleanConfigOption.Builder().parent(COMBAT_GROUP).id("durability_removal").value(false).icon(new ItemStack(Items.ANVIL)).build();
    public static final RangedIntConfigOption ENDER_PEARL_COOLDOWN = new RangedIntConfigOption.Builder().parent(COMBAT_GROUP).id("ender_pearl_cooldown").value(0).icon(new ItemStack(Items.ENDER_PEARL)).min(0).max(600).build();
    public static final RangedFloatConfigOption ENDER_PEARL_SPEED = new RangedFloatConfigOption.Builder().parent(COMBAT_GROUP).id("ender_pearl_speed").value(1.5f).icon(new ItemStack(Items.ENDER_PEARL)).min(0f).max(10f).build();
    public static final RangedIntConfigOption END_CRYSTAL_COOLDOWN = new RangedIntConfigOption.Builder().parent(COMBAT_GROUP).id("end_crystal_cooldown").value(0).icon(new ItemStack(Items.END_CRYSTAL)).min(0).max(600).build();
    public static final RangedFloatConfigOption END_CRYSTAL_POWER = new RangedFloatConfigOption.Builder().parent(COMBAT_GROUP).id("end_crystal_power").value(6f).icon(new ItemStack(Items.END_CRYSTAL)).min(0f).max(20f).build();
    public static final BooleanConfigOption CROSSBOW_END_CRYSTALS = new BooleanConfigOption.Builder().parent(COMBAT_GROUP).id("crossbow_end_crystals").value(false).icon(new ItemStack(Items.END_CRYSTAL)).build();
    public static final BooleanConfigOption MACE_REBALANCE = new BooleanConfigOption.Builder().parent(COMBAT_GROUP).id("mace_rebalance").value(true).icon(new ItemStack(Items.MACE)).build();
    public static final BooleanConfigOption SWEEPING_MARGIN = new BooleanConfigOption.Builder().parent(COMBAT_GROUP).id("sweeping_margin").value(false).icon(new ItemStack(Combat.DIAMOND_GLAIVE)).build();
    public static final RangedFloatConfigOption SWEEP_MARGIN_MULTIPLIER = new RangedFloatConfigOption.Builder().parent(COMBAT_GROUP).id("sweep_margin_multiplier").value(0.05f).icon(new ItemStack(Combat.DIAMOND_GLAIVE)).min(0f).max(5f).build();
    public static final BooleanConfigOption RESPAWN_ANCHORS_DONT_EXPLODE = new BooleanConfigOption.Builder().parent(COMBAT_GROUP).id("respawn_anchors_dont_explode").value(false).icon(new ItemStack(Items.RESPAWN_ANCHOR)).build();
    public static final RangedIntConfigOption SHIELD_MAX_DURABILITY = new RangedIntConfigOption.Builder().parent(COMBAT_GROUP).id("shield_max_durability").value(30).icon(new ItemStack(Items.SHIELD)).min(0).max(200).build();
    public static final RangedIntConfigOption SHIELD_LOWER_COOLDOWN = new RangedIntConfigOption.Builder().parent(COMBAT_GROUP).id("shield_lower_cooldown").value(60).icon(new ItemStack(Items.SHIELD)).min(0).max(600).build();
    public static final RangedIntConfigOption SPLASH_POTION_COOLDOWN = new RangedIntConfigOption.Builder().parent(COMBAT_GROUP).id("splash_potion_cooldown").value(0).icon(new ItemStack(Items.SHIELD)).min(0).max(600).build();

    public static final BooleanConfigOption DISABLED_ARMOR = new BooleanConfigOption.Builder().parent(ARMOR_GROUP).id("disabled_armor").value(false).icon(new ItemStack(Items.DIAMOND_CHESTPLATE)).build();

    public static final BooleanConfigOption ULTRA_DARK = new BooleanConfigOption.Builder().parent(CHALLENGES_GROUP).id("ultra_dark").value(false).icon(new ItemStack(Items.SCULK)).build();
    public static final BooleanConfigOption DISABLED_CHAT = new BooleanConfigOption.Builder().parent(CHALLENGES_GROUP).id("disabled_chat").value(false).icon(new ItemStack(Items.OAK_SIGN)).build();

    public static final BooleanConfigOption EXTRA_STONE_BRICKS = new BooleanConfigOption.Builder().parent(BUILDING_GROUP).id("extra_stone_bricks").value(true).icon(new ItemStack(Building.GRANITE_BRICKS.getBaseBlock())).build();

    public static final RangedIntConfigOption METAL_DETECTOR_RADIUS = new RangedIntConfigOption.Builder().parent(EXPLORING_GROUP).id("metal_detector_radius").value(16).icon(new ItemStack(Exploring.METAL_DETECTOR_ITEM)).min(0).max(50).build();
    public static final RangedIntConfigOption METAL_DETECTOR_HEIGHT = new RangedIntConfigOption.Builder().parent(EXPLORING_GROUP).id("metal_detector_height").value(2).icon(new ItemStack(Exploring.METAL_DETECTOR_ITEM)).min(0).max(50).build();
    public static final BooleanConfigOption SPAWN_IN_VILLAGE = new BooleanConfigOption.Builder().parent(EXPLORING_GROUP).id("spawn_in_village").value(true).icon(new ItemStack(Items.VILLAGER_SPAWN_EGG)).build();
    public static final BooleanConfigOption FERTILIZABLE_SUGARCANE = new BooleanConfigOption.Builder().parent(EXPLORING_GROUP).id("fertilizable_sugarcane").value(false).icon(new ItemStack(Items.BONE_MEAL)).build();
    public static final RangedIntConfigOption MAX_SUGARCANE_HEIGHT = new RangedIntConfigOption.Builder().parent(EXPLORING_GROUP).id("max_sugarcane_height").value(3).icon(new ItemStack(Items.SUGAR_CANE)).min(2).max(30).build();

    public static final BooleanConfigOption ENCHANTMENT_CATALYSTS = new BooleanConfigOption.Builder().parent(ENCHANTING_GROUP).id("catalysts").value(false).icon(new ItemStack(Items.BLAZE_POWDER)).build();
    public static final BooleanConfigOption ENCHANTMENT_CAPACITY = new BooleanConfigOption.Builder().parent(ENCHANTING_GROUP).id("capacity").value(false).icon(new ItemStack(Items.ENCHANTING_TABLE)).build();

    public static final BooleanConfigOption CROP_COMPARATOR_OUTPUT = new BooleanConfigOption.Builder().parent(REDSTONE_GROUP).id("crop_comparator_output").value(false).icon(new ItemStack(Items.COMPARATOR)).build();
    public static final BooleanConfigOption DISPENSERS_PLACE_BLOCKS = new BooleanConfigOption.Builder().parent(REDSTONE_GROUP).id("dispensers_place_blocks").value(false).icon(new ItemStack(Items.DISPENSER)).build();
    public static final RangedIntConfigOption HOPPER_COOLDOWN = new RangedIntConfigOption.Builder().parent(REDSTONE_GROUP).id("hopper_cooldown").value(8).icon(new ItemStack(Items.HOPPER)).min(1).max(20).build();
    public static final BooleanConfigOption INSTANT_CHAIN_TNT = new BooleanConfigOption.Builder().parent(REDSTONE_GROUP).id("instant_chain_tnt").value(false).icon(new ItemStack(Items.TNT)).build();

    public static final BooleanConfigOption HUD_ITEM_TOOLTIPS = new BooleanConfigOption.Builder().parent(CLIENT_GROUP).id("hud_item_tooltips").value(false).icon(new ItemStack(Items.SPYGLASS)).build();
    public static final BooleanConfigOption COMPASS_COORDS = new BooleanConfigOption.Builder().parent(CLIENT_GROUP).id("compass_corrds").value(true).icon(new ItemStack(Items.COMPASS)).build();

    public static float ultraDarkBlend(World world) {
        if (!ULTRA_DARK.get()) {
            return 0;
        }

        float f = (world.getTimeOfDay() / 12000f + 0.75f) * (float) Math.PI;
        f = (float) (Math.cos(f) * 1.5f);
        f = MathHelper.clamp(f, 0, 1);
        return f;
    }

    public static Text print() {
        MutableText text = Text.translatable("config.beryllium.title");

        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            text.append(Text.literal("\n").append(option.name()).append(" = ").append(option.get().toString()));
        }

        return text;
    }

    public static LiteralArgumentBuilder<ServerCommandSource> command(LiteralArgumentBuilder<ServerCommandSource> command) {
        command.then(LiteralArgumentBuilder.<ServerCommandSource>literal("config")
                .executes(context -> {
                    context.getSource().sendFeedback(BerylliumConfig::print, false);
                    return 1;
                }));

        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            command.then(
                    LiteralArgumentBuilder.<ServerCommandSource>literal(option.id.toString())
                            .requires(context -> context.hasPermissionLevel(2))
                            .executes(context -> {
                                context.getSource().sendFeedback(() -> option.name().copy().append(" is set to ").append(option.get().toString()), false);
                                return 1;
                            })
                            .then(
                                    CommandManager.argument("value", option.argumentType)
                                            .executes(context -> {
                                                context.getSource().getServer().execute(() -> {
                                                    Object value = context.getArgument("value", Object.class);
                                                    option.set(value);
                                                    option.updatedServer(context.getSource().getServer());

                                                    if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
                                                        for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
                                                            ServerPlayNetworking.send(player, new SetConfigValuePacket<>(option));
                                                        }
                                                    }

                                                    context.getSource().sendFeedback(() -> option.name().copy().append(" now set to ").append(option.get().toString()), true);
                                                });
                                                return 1;
                                            })
                            )
            );
        }

        return command;
    }

    @Override
    public void onInitialize() {
    }
}
