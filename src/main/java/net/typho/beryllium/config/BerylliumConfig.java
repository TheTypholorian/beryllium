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

    public static final RootConfigOptionGroup ROOT_GROUP = new RootConfigOptionGroup(new ItemStack(Items.END_CRYSTAL), Beryllium.BASE_CONSTRUCTOR.id("config"));

    public static final ConfigOptionGroup ARMOR_GROUP = new ConfigOptionGroup(new ItemStack(Items.DIAMOND_CHESTPLATE), ROOT_GROUP, "armor");
    public static final ConfigOptionGroup BUILDING_GROUP = new ConfigOptionGroup(new ItemStack(Items.BRICKS), ROOT_GROUP, "building");
    public static final ConfigOptionGroup CHALLENGES_GROUP = new ConfigOptionGroup(new ItemStack(Items.TRIAL_KEY), ROOT_GROUP, "challenges");
    public static final ConfigOptionGroup COMBAT_GROUP = new ConfigOptionGroup(new ItemStack(Items.DIAMOND_SWORD), ROOT_GROUP, "combat");
    public static final ConfigOptionGroup ENCHANTING_GROUP = new ConfigOptionGroup(new ItemStack(Items.ENCHANTED_BOOK), ROOT_GROUP, "enchanting");
    public static final ConfigOptionGroup EXPLORING_GROUP = new ConfigOptionGroup(new ItemStack(Items.FILLED_MAP), ROOT_GROUP, "exploring");
    public static final ConfigOptionGroup REDSTONE_GROUP = new ConfigOptionGroup(new ItemStack(Items.REDSTONE), ROOT_GROUP, "redstone");
    public static final ConfigOptionGroup CLIENT_GROUP = new ConfigOptionGroup(EnvType.CLIENT, new ItemStack(Items.PAPER), ROOT_GROUP, "client");

    public static final BooleanConfigOption DURABILITY_REMOVAL = new BooleanConfigOption(COMBAT_GROUP, "durability_removal", false, new ItemStack(Items.ANVIL));
    public static final RangedIntConfigOption ENDER_PEARL_COOLDOWN = new RangedIntConfigOption(COMBAT_GROUP, "ender_pearl_cooldown", 0, new ItemStack(Items.ENDER_PEARL), 0, 600);
    public static final RangedFloatConfigOption ENDER_PEARL_SPEED = new RangedFloatConfigOption(COMBAT_GROUP, "ender_pearl_speed", 1.5f, new ItemStack(Items.ENDER_PEARL), 0f, 10f);
    public static final RangedIntConfigOption END_CRYSTAL_COOLDOWN = new RangedIntConfigOption(COMBAT_GROUP, "end_crystal_cooldown", 0, new ItemStack(Items.END_CRYSTAL), 0, 600);
    public static final RangedFloatConfigOption END_CRYSTAL_POWER = new RangedFloatConfigOption(COMBAT_GROUP, "end_crystal_power", 6f, new ItemStack(Items.END_CRYSTAL), 0f, 20f);
    public static final BooleanConfigOption CROSSBOW_END_CRYSTALS = new BooleanConfigOption(COMBAT_GROUP, "crossbow_end_crystals", false, new ItemStack(Items.END_CRYSTAL));
    public static final BooleanConfigOption MACE_REBALANCE = new BooleanConfigOption(COMBAT_GROUP, "mace_rebalance", true, new ItemStack(Items.MACE));
    public static final BooleanConfigOption SWEEPING_MARGIN = new BooleanConfigOption(COMBAT_GROUP, "sweeping_margin", false, new ItemStack(Combat.DIAMOND_GLAIVE));
    public static final RangedFloatConfigOption SWEEP_MARGIN_MULTIPLIER = new RangedFloatConfigOption(COMBAT_GROUP, "sweep_margin_multiplier", 0.05f, new ItemStack(Combat.DIAMOND_GLAIVE), 0f, 5f);
    public static final BooleanConfigOption RESPAWN_ANCHORS_DONT_EXPLODE = new BooleanConfigOption(COMBAT_GROUP, "respawn_anchors_dont_explode", false, new ItemStack(Items.RESPAWN_ANCHOR));
    public static final RangedIntConfigOption SHIELD_MAX_DURABILITY = new RangedIntConfigOption(COMBAT_GROUP, "shield_max_durability", 30, new ItemStack(Items.SHIELD), 0, 200);
    public static final RangedIntConfigOption SHIELD_LOWER_COOLDOWN = new RangedIntConfigOption(COMBAT_GROUP, "shield_lower_cooldown", 60, new ItemStack(Items.SHIELD), 0, 600);
    public static final RangedIntConfigOption SPLASH_POTION_COOLDOWN = new RangedIntConfigOption(COMBAT_GROUP, "splash_potion_cooldown", 0, new ItemStack(Items.SHIELD), 0, 600);

    public static final BooleanConfigOption DISABLED_ARMOR = new BooleanConfigOption(ARMOR_GROUP, "disabled_armor", false, new ItemStack(Items.DIAMOND_CHESTPLATE));

    public static final BooleanConfigOption ULTRA_DARK = new BooleanConfigOption(CHALLENGES_GROUP, "ultra_dark", false, new ItemStack(Items.SCULK));
    public static final BooleanConfigOption DISABLED_CHAT = new BooleanConfigOption(CHALLENGES_GROUP, "disabled_chat", false, new ItemStack(Items.OAK_SIGN));

    public static final BooleanConfigOption EXTRA_STONE_BRICKS = new BooleanConfigOption(BUILDING_GROUP, "extra_stone_bricks", true, new ItemStack(Building.GRANITE_BRICKS.getBaseBlock()));

    public static final RangedIntConfigOption METAL_DETECTOR_RADIUS = new RangedIntConfigOption(EXPLORING_GROUP, "metal_detector_radius", 16, new ItemStack(Exploring.METAL_DETECTOR_ITEM), 0, 50);
    public static final RangedIntConfigOption METAL_DETECTOR_HEIGHT = new RangedIntConfigOption(EXPLORING_GROUP, "metal_detector_height", 2, new ItemStack(Exploring.METAL_DETECTOR_ITEM), 0, 50);
    public static final BooleanConfigOption SPAWN_IN_VILLAGE = new BooleanConfigOption(EXPLORING_GROUP, "spawn_in_village", true, new ItemStack(Items.VILLAGER_SPAWN_EGG));
    public static final BooleanConfigOption FERTILIZABLE_SUGARCANE = new BooleanConfigOption(EXPLORING_GROUP, "fertilizable_sugarcane", false, new ItemStack(Items.BONE_MEAL));
    public static final RangedIntConfigOption MAX_SUGARCANE_HEIGHT = new RangedIntConfigOption(EXPLORING_GROUP, "max_sugarcane_height", 3, new ItemStack(Items.SUGAR_CANE), 2, 30);

    public static final BooleanConfigOption ENCHANTMENT_CATALYSTS = new BooleanConfigOption(ENCHANTING_GROUP, "catalysts", false, new ItemStack(Items.BLAZE_POWDER));
    public static final BooleanConfigOption ENCHANTMENT_CAPACITY = new BooleanConfigOption(ENCHANTING_GROUP, "capacity", false, new ItemStack(Items.ENCHANTING_TABLE));

    public static final BooleanConfigOption CROP_COMPARATOR_OUTPUT = new BooleanConfigOption(REDSTONE_GROUP, "crop_comparator_output", false, new ItemStack(Items.COMPARATOR));
    public static final BooleanConfigOption DISPENSERS_PLACE_BLOCKS = new BooleanConfigOption(REDSTONE_GROUP, "dispensers_place_blocks", false, new ItemStack(Items.DISPENSER));
    public static final RangedIntConfigOption HOPPER_COOLDOWN = new RangedIntConfigOption(REDSTONE_GROUP, "hopper_cooldown", 8, new ItemStack(Items.HOPPER), 1, 20);
    public static final BooleanConfigOption INSTANT_CHAIN_TNT = new BooleanConfigOption(REDSTONE_GROUP, "instant_chain_tnt", false, new ItemStack(Items.TNT));

    public static final BooleanConfigOption HUD_ITEM_TOOLTIPS = new BooleanConfigOption(CLIENT_GROUP, "hud_item_tooltips", false, new ItemStack(Items.SPYGLASS));
    public static final BooleanConfigOption COMPASS_COORDS = new BooleanConfigOption(CLIENT_GROUP, "compass_corrds", true, new ItemStack(Items.COMPASS));

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
            text.append(Text.literal("\n").append(option.name).append(" = ").append(option.get().toString()));
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
