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

        /*
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir")).getParent();
            Path srcJava = projectRoot.resolve("src").resolve("main").resolve("java");
            try (Stream<Path> paths = Files.walk(srcJava)) {
                System.out.println(paths
                        .filter(Files::isRegularFile)
                        .mapToLong(path -> {
                            try {
                                return Files.lines(path).count();
                            } catch (IOException e) {
                                return 0L;
                            }
                        })
                        .sum());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
    }

    public static class BerylliumConfig extends Config {
        public Exploring.Config exploring = new Exploring.Config();
        public Combat.Config combat = new Combat.Config();
        public boolean durabilityRemoval = true;

        public BerylliumConfig() {
            super(Identifier.of(MOD_ID, "config"));
        }
    }
}
