package net.typho.beryllium.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ServerConfig implements ModInitializer {
    public static final Map<Identifier, ConfigOption<?>> ALL_OPTIONS = new LinkedHashMap<>();

    public static final Set<Identifier> DURABILITY_ENCHANTS = new HashSet<>(Set.of(
            Identifier.ofVanilla("mending"),
            Identifier.ofVanilla("unbreaking")
    ));

    public static final ConfigOptionGroup ARMOR_GROUP = new ConfigOptionGroup(new ItemStack(Items.DIAMOND_CHESTPLATE), Beryllium.ARMOR_CONSTRUCTOR, "");
    public static final ConfigOptionGroup BUILDING_GROUP = new ConfigOptionGroup(new ItemStack(Items.BRICKS), Beryllium.BUILDING_CONSTRUCTOR, "");
    public static final ConfigOptionGroup CHALLENGES_GROUP = new ConfigOptionGroup(new ItemStack(Items.TRIAL_KEY), Beryllium.CHALLENGES_CONSTRUCTOR, "");
    public static final ConfigOptionGroup COMBAT_GROUP = new ConfigOptionGroup(new ItemStack(Items.DIAMOND_SWORD), Beryllium.COMBAT_CONSTRUCTOR, "");
    public static final ConfigOptionGroup ENCHANTING_GROUP = new ConfigOptionGroup(new ItemStack(Items.ENCHANTED_BOOK), Beryllium.ENCHANTING_CONSTRUCTOR, "");
    public static final ConfigOptionGroup EXPLORING_GROUP = new ConfigOptionGroup(new ItemStack(Items.FILLED_MAP), Beryllium.EXPLORING_CONSTRUCTOR, "");
    public static final ConfigOptionGroup REDSTONE_GROUP = new ConfigOptionGroup(new ItemStack(Items.REDSTONE), Beryllium.REDSTONE_CONSTRUCTOR, "");

    public static final ConfigOptionGroup BASE_GROUP = new ConfigOptionGroup(
            ItemStack.EMPTY, Beryllium.BASE_CONSTRUCTOR, "config",
            ARMOR_GROUP,
            BUILDING_GROUP,
            CHALLENGES_GROUP,
            COMBAT_GROUP,
            ENCHANTING_GROUP,
            EXPLORING_GROUP,
            REDSTONE_GROUP
    );

    public static final BooleanConfigOption DURABILITY_REMOVAL = new BooleanConfigOption(new ItemStack(Items.ANVIL), COMBAT_GROUP, "durability_removal", false);
    public static final RangedIntConfigOption ENDER_PEARL_COOLDOWN = new RangedIntConfigOption(new ItemStack(Items.ENDER_PEARL), COMBAT_GROUP, "ender_pearl_cooldown", 0, 0, 600);
    public static final RangedFloatConfigOption ENDER_PEARL_SPEED = new RangedFloatConfigOption(new ItemStack(Items.ENDER_PEARL), COMBAT_GROUP, "ender_pearl_speed", 1.5f, 0f, 10f);
    public static final RangedIntConfigOption END_CRYSTAL_COOLDOWN = new RangedIntConfigOption(new ItemStack(Items.END_CRYSTAL), COMBAT_GROUP, "end_crystal_cooldown", 0, 0, 600);
    public static final RangedFloatConfigOption END_CRYSTAL_POWER = new RangedFloatConfigOption(new ItemStack(Items.END_CRYSTAL), COMBAT_GROUP, "end_crystal_power", 6f, 0f, 20f);
    public static final BooleanConfigOption CROSSBOW_END_CRYSTALS = new BooleanConfigOption(new ItemStack(Items.END_CRYSTAL), COMBAT_GROUP, "crossbow_end_crystals", false);
    public static final BooleanConfigOption MACE_REBALANCE = new BooleanConfigOption(new ItemStack(Items.MACE), COMBAT_GROUP, "mace_rebalance", true);
    public static final BooleanConfigOption SWEEPING_MARGIN = new BooleanConfigOption(new ItemStack(Combat.DIAMOND_GLAIVE), COMBAT_GROUP, "sweeping_margin", false);
    public static final RangedFloatConfigOption SWEEP_MARGIN_MULTIPLIER = new RangedFloatConfigOption(new ItemStack(Combat.DIAMOND_GLAIVE), COMBAT_GROUP, "sweep_margin_multiplier", 0.05f, 0f, 5f);
    public static final BooleanConfigOption RESPAWN_ANCHORS_DONT_EXPLODE = new BooleanConfigOption(new ItemStack(Items.RESPAWN_ANCHOR), COMBAT_GROUP, "respawn_anchors_dont_explode", false);
    public static final RangedIntConfigOption SHIELD_MAX_DURABILITY = new RangedIntConfigOption(new ItemStack(Items.SHIELD), COMBAT_GROUP, "shield_max_durability", 30, 0, 200);
    public static final RangedIntConfigOption SHIELD_LOWER_COOLDOWN = new RangedIntConfigOption(new ItemStack(Items.SHIELD), COMBAT_GROUP, "shield_lower_cooldown", 60, 0, 600);
    public static final RangedIntConfigOption SPLASH_POTION_COOLDOWN = new RangedIntConfigOption(new ItemStack(Items.SHIELD), COMBAT_GROUP, "splash_potion_cooldown", 0, 0, 600);

    public static final BooleanConfigOption DISABLED_ARMOR = new BooleanConfigOption(new ItemStack(Items.DIAMOND_CHESTPLATE), ARMOR_GROUP, "disabled_armor", false);

    public static final BooleanConfigOption ULTRA_DARK = new BooleanConfigOption(new ItemStack(Items.SCULK), CHALLENGES_GROUP, "ultra_dark", false);
    public static final BooleanConfigOption DISABLED_CHAT = new BooleanConfigOption(new ItemStack(Items.OAK_SIGN), CHALLENGES_GROUP, "disabled_chat", false);

    public static final BooleanConfigOption EXTRA_STONE_BRICKS = new BooleanConfigOption(new ItemStack(Building.GRANITE_BRICKS.getBaseBlock()), BUILDING_GROUP, "extra_stone_bricks", true);

    public static final RangedIntConfigOption METAL_DETECTOR_RADIUS = new RangedIntConfigOption(new ItemStack(Exploring.METAL_DETECTOR_ITEM), EXPLORING_GROUP, "metal_detector_radius", 16, 0, 50);
    public static final RangedIntConfigOption METAL_DETECTOR_HEIGHT = new RangedIntConfigOption(new ItemStack(Exploring.METAL_DETECTOR_ITEM), EXPLORING_GROUP, "metal_detector_height", 2, 0, 50);
    public static final BooleanConfigOption SPAWN_IN_VILLAGE = new BooleanConfigOption(new ItemStack(Items.VILLAGER_SPAWN_EGG), EXPLORING_GROUP, "spawn_in_village", true);
    public static final BooleanConfigOption FERTILIZABLE_SUGARCANE = new BooleanConfigOption(new ItemStack(Items.BONE_MEAL), EXPLORING_GROUP, "fertilizable_sugarcane", false);
    public static final RangedIntConfigOption MAX_SUGARCANE_HEIGHT = new RangedIntConfigOption(new ItemStack(Items.SUGAR_CANE), EXPLORING_GROUP, "max_sugarcane_height", 3, 2, 30);

    public static final BooleanConfigOption ENCHANTMENT_CATALYSTS = new BooleanConfigOption(new ItemStack(Items.BLAZE_POWDER), ENCHANTING_GROUP, "catalysts", false);
    public static final BooleanConfigOption ENCHANTMENT_CAPACITY = new BooleanConfigOption(new ItemStack(Items.ENCHANTING_TABLE), ENCHANTING_GROUP, "capacity", false);

    public static final BooleanConfigOption CROP_COMPARATOR_OUTPUT = new BooleanConfigOption(new ItemStack(Items.COMPARATOR), REDSTONE_GROUP, "crop_comparator_output", false);
    public static final BooleanConfigOption DISPENSERS_PLACE_BLOCKS = new BooleanConfigOption(new ItemStack(Items.DISPENSER), REDSTONE_GROUP, "dispensers_place_blocks", false);
    public static final RangedIntConfigOption HOPPER_COOLDOWN = new RangedIntConfigOption(new ItemStack(Items.HOPPER), REDSTONE_GROUP, "hopper_cooldown", 8, 1, 20);
    public static final BooleanConfigOption INSTANT_CHAIN_TNT = new BooleanConfigOption(new ItemStack(Items.TNT), REDSTONE_GROUP, "instant_chain_tnt", false);

    public static float ultraDarkBlend(World world) {
        if (!ULTRA_DARK.get()) {
            return 0;
        }

        float f = (world.getTimeOfDay() / 12000f + 0.75f) * (float) Math.PI;
        f = (float) (Math.cos(f) * 1.5f);
        f = MathHelper.clamp(f, 0, 1);
        return f;
    }

    public static void read(Dynamic<?> dynamic) {
        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            dynamic.get(option.id.toString()).get().ifSuccess(result -> {
                DataResult<? extends Pair<?, ?>> result1 = option.codec().decode(result);

                if (result1.isSuccess()) {
                    option.set(result1.getOrThrow().getFirst());
                } else {
                    System.err.println("Error reading server config [" + option.id + "]: " + result1.error().orElseThrow().message());
                }
            });
        }
    }

    private static <T> void write(ConfigOption<T> option, NbtCompound nbt) {
        if (option.defValue == null || !option.defValue.equals(option.value)) {
            option.codec().encodeStart(NbtOps.INSTANCE, option.get())
                    .result().ifPresent(elem -> nbt.put(option.id.toString(), elem));
        }
    }

    public static NbtElement write() {
        NbtCompound nbt = new NbtCompound();

        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            write(option, nbt);
        }

        return nbt;
    }

    public static Text print() {
        MutableText text = Text.translatable("config.beryllium.title");

        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            text.append(Text.literal("\n").append(option.display()).append(" = ").append(option.value.toString()));
        }

        return text;
    }

    public static LiteralArgumentBuilder<ServerCommandSource> command(LiteralArgumentBuilder<ServerCommandSource> command) {
        command.then(LiteralArgumentBuilder.<ServerCommandSource>literal("config")
                .executes(context -> {
                    context.getSource().sendFeedback(ServerConfig::print, false);
                    return 1;
                }));

        for (ConfigOption<?> option : ALL_OPTIONS.values()) {
            command.then(
                    LiteralArgumentBuilder.<ServerCommandSource>literal(option.commandPath())
                            .requires(context -> context.hasPermissionLevel(2))
                            .executes(context -> {
                                context.getSource().sendFeedback(() -> option.display().copy().append(" is set to ").append(option.get().toString()), false);
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

                                                    context.getSource().sendFeedback(() -> option.display().copy().append(" now set to ").append(option.get().toString()), true);
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
