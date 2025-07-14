package net.typho.beryllium.mixin.exploring;

import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.typho.beryllium.exploring.HeightRangePlacementModifierAccessor;
import org.spongepowered.asm.mixin.*;

@Mixin(HeightRangePlacementModifier.class)
@Implements(@Interface(iface = HeightRangePlacementModifierAccessor.class, prefix = "access$"))
public class HeightRangePlacementModifierMixin {
    @Shadow @Final private HeightProvider height;

    public HeightProvider access$height() {
        return height;
    }
}
