package net.typho.beryllium.exploring;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.typho.beryllium.Beryllium;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class BiomeInit implements TerraBlenderApi {
    private static final MaterialRules.MaterialRule BONE_BLOCK = MaterialRules.block(Blocks.BONE_BLOCK.getDefaultState());

    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new NetherRegion(Beryllium.EXPLORING.id("nether"), RegionType.NETHER, 4));

        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, Beryllium.MOD_ID, MaterialRules.sequence(
                BONE_BLOCK
        ));
    }
}
