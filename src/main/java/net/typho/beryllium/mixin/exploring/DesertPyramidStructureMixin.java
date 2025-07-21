package net.typho.beryllium.mixin.exploring;

import net.minecraft.world.gen.structure.DesertPyramidStructure;
import net.typho.beryllium.Beryllium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DesertPyramidStructure.class)
public class DesertPyramidStructureMixin {
    @ModifyConstant(
            method = "<init>",
            constant = {@Constant(intValue = 21)}
    )
    private static int init(int constant) {
        if (Beryllium.CONFIG.exploring.structures.desertPyramid) {
            return 23;
        }

        return constant;
    }
}
