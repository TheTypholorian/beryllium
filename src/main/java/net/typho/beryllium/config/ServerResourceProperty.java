package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public abstract class ServerResourceProperty<O> extends Property<O> {
    public ServerResourceProperty(Identifier id, ArgumentType<O> argumentType, O value) {
        super(id, argumentType, value);
    }

    @Override
    public void updatedServer(MinecraftServer server) {
        server.reloadResources(server.getDataPackManager().getEnabledIds());
    }
}
