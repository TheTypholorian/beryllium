package net.typho.beryllium.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FeatureProperty extends BooleanServerResourceProperty {
    public FeatureFlag flag;

    public FeatureProperty(Identifier id, Boolean value) {
        super(id, value);
    }

    @Override
    public void updatedClient(MinecraftClient client) {
        super.updatedClient(client);
        client.player.sendMessage(Text.translatable("config.beryllium.feature_warning"));
    }

    @Override
    public void updatedServer(MinecraftServer server) {
        super.updatedServer(server);
        server.sendMessage(Text.translatable("config.beryllium.feature_warning"));
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
