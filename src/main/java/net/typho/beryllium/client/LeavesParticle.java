package net.typho.beryllium.client;

import net.minecraft.client.particle.CherryLeavesParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class LeavesParticle extends CherryLeavesParticle {
    protected LeavesParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider);
    }
}
