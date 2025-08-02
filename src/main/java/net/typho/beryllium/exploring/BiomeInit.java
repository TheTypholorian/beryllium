package net.typho.beryllium.exploring;

import net.typho.beryllium.Beryllium;
import terrablender.api.EndBiomeRegistry;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class BiomeInit implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.END, Beryllium.MOD_ID, Beryllium.EXPLORING.createEndRule());
        EndBiomeRegistry.registerHighlandsBiome(Beryllium.EXPLORING.CORRUPTED_FOREST, 3);
    }
}
