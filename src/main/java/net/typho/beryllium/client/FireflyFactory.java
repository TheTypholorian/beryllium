package net.typho.beryllium.client;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.world.Heightmap;
import net.typho.beryllium.exploring.FireflyParticle;

public class FireflyFactory implements ParticleFactory<SimpleParticleType> {
    public final FabricSpriteProvider sprites;

    public FireflyFactory(FabricSpriteProvider sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double dx, double dy, double dz) {
        float sky = world.getSkyAngle(1);

        if (sky < 0.3 || sky > 0.7) {
            return null;
        }

        int surf = world.getTopY(Heightmap.Type.WORLD_SURFACE, (int) Math.floor(x), (int) Math.floor(z));

        if (y > surf + 5) {
            return null;
        }

        FireflyParticle particle = new FireflyParticle(world, x, y, z, 0.01, 0.01, 0.01);
        particle.setSprite(sprites);
        return particle;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }
}
