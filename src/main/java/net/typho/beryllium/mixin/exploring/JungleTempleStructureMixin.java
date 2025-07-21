package net.typho.beryllium.mixin.exploring;

import net.minecraft.world.gen.structure.JungleTempleStructure;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(JungleTempleStructure.class)
public class JungleTempleStructureMixin {
    @ModifyConstant(
            method = "<init>",
            constant = {@Constant(intValue = 15), @Constant(intValue = 12)}
    )
    private static int init(int constant) {
        if (Beryllium.CONFIG.exploring.structures.junglePyramid) {
            return 16;
        }

        return constant;
    }
}
