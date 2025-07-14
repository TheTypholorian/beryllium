package net.typho.beryllium.exploring;

import net.minecraft.world.gen.heightprovider.HeightProvider;

public interface HeightRangePlacementModifierAccessor {
    HeightProvider height();

    static HeightRangePlacementModifierAccessor cast(Object o) {
        return (HeightRangePlacementModifierAccessor) o;
    }
}
