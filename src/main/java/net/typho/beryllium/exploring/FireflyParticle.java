package net.typho.beryllium.exploring;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;

import java.util.Random;

public class FireflyParticle extends SpriteBillboardParticle {
    public FireflyParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    public FireflyParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.velocityX = (Math.random() * 2.0 - 1.0) * g;
        this.velocityY = (Math.random() * 2.0 - 1.0) * h;
        this.velocityZ = (Math.random() * 2.0 - 1.0) * i;
    }

    {
        maxAge = 50 * (new Random().nextInt(10));
        collidesWithWorld = false;
        scale(0.2f);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getBrightness(float tickDelta) {
        float f = (float) ((Math.sin(Math.PI * (age / 25d - 0.5)) + 1) / 2);
        int max = 0xF0;
        int i = (int) (f * max);

        return (i << 16) | i;
    }
}
