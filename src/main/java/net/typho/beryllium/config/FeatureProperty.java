package net.typho.beryllium.config;

import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class FeatureProperty extends BooleanProperty {
    public FeatureFlag flag;

    public FeatureProperty(Identifier id, Boolean value) {
        super(id, value);
    }

    @Override
    public void updatedServer(MinecraftServer server) {
        server.reloadResources(server.getDataPackManager().getEnabledIds());
    }

    @Override
    public FeatureSet getFeatures(FeatureSet features) {
        if (get()) {
            return features.combine(FeatureSet.of(flag));
        } else {
            return features.subtract(FeatureSet.of(flag));
        }
    }
}
