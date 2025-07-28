package net.typho.beryllium;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.typho.beryllium.building.Building;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.food.Food;
import net.typho.beryllium.redstone.Redstone;

public class Beryllium implements ModInitializer {
    public static final String MOD_ID = "beryllium";

    public static final BerylliumConfig CONFIG = ConfigApiJava.registerAndLoadConfig(BerylliumConfig::new);
    public static final Exploring EXPLORING = new Exploring("exploring");
    public static final Enchanting ENCHANTING = new Enchanting("enchanting");
    public static final Combat COMBAT = new Combat("combat");
    public static final Building BUILDING = new Building("building");
    public static final Food FOOD = new Food("food");
    public static final Redstone REDSTONE = new Redstone("redstone");

    @Override
    public void onInitialize() {
        ENCHANTING.onInitialize();
        COMBAT.onInitialize();
        EXPLORING.onInitialize();
        BUILDING.onInitialize();
        FOOD.onInitialize();
        REDSTONE.onInitialize();
    }

    public static class BerylliumConfig extends Config {
        public Combat.Config combat = new Combat.Config();
        public Enchanting.Config enchanting = new Enchanting.Config();
        public Exploring.Config exploring = new Exploring.Config();
        public Food.Config food = new Food.Config();
        public Redstone.Config redstone = new Redstone.Config();
        public boolean durabilityRemoval = true;

        public BerylliumConfig() {
            super(Identifier.of(MOD_ID, "common"));
        }
    }
}
