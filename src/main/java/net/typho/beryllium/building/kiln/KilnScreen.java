package net.typho.beryllium.building.kiln;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.Beryllium;

@Environment(EnvType.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<KilnScreenHandler> {
    private static final Identifier TEXTURE = Beryllium.BUILDING.id("textures/gui/container/kiln.png");

    public KilnScreen(KilnScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new KilnRecipeBookScreen(), inventory, title, TEXTURE, Identifier.ofVanilla("container/blast_furnace/lit_progress"), Identifier.ofVanilla("container/blast_furnace/burn_progress"));
    }
}
