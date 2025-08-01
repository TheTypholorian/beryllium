package net.typho.beryllium.exploring;

import terrablender.api.TerraBlenderApi;

public class BiomeInit implements TerraBlenderApi {
    //private static final MaterialRules.MaterialRule BONE_BLOCK = MaterialRules.block(Blocks.BONE_BLOCK.getDefaultState());

    @Override
    public void onTerraBlenderInitialized() {
        //Regions.register(new NetherRegion(Beryllium.EXPLORING.id("nether"), RegionType.NETHER, 4));

        //SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, Beryllium.MOD_ID, MaterialRules.sequence(
        //        BONE_BLOCK
        //));
    }
}
