package net.typho.beryllium.config;

import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;
import net.typho.beryllium.enchanting.Enchanting;
import net.typho.beryllium.exploring.Exploring;
import net.typho.beryllium.redstone.Redstone;

public class BerylliumConfig extends Config {
    public Enchanting.Config enchanting = new Enchanting.Config();
    public Exploring.Config exploring = new Exploring.Config();
    public Redstone.Config redstone = new Redstone.Config();

    public BerylliumConfig() {
        super(Identifier.of(Beryllium.MOD_ID, "common"));
    }
}
