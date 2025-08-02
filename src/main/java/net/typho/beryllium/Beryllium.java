package net.typho.beryllium;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.util.Identifier;
import net.typho.beryllium.combat.Combat;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.food.Food;
import net.typho.beryllium.redstone.Redstone;

public class Beryllium {
    public static final String MOD_ID = "beryllium";

    public static final BerylliumConfig CONFIG = ConfigApiJava.registerAndLoadConfig(BerylliumConfig::new);

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
