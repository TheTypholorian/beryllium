package net.typho.beryllium.end_city

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth

class GusterParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    val sprites: SpriteSet
) : TextureSheetParticle(level, x, y, z) {
    init {
        setSpriteFromAge(sprites)
        lifetime = 50 + random.nextInt(4)
        xd = xSpeed
        yd = ySpeed
        zd = zSpeed
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_OPAQUE

    override fun tick() {
        super.tick()
        setSpriteFromAge(sprites)
    }

    override fun getQuadSize(scaleFactor: Float): Float {
        return quadSize * Mth.clamp(
            (age.toFloat() + scaleFactor) / lifetime.toFloat() * 32.0f,
            0.0f,
            1.0f
        )
    }

    class Provider(val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpped: Double,
            zSpeed: Double
        ): Particle = GusterParticle(level, x, y, z, xSpeed, ySpped, zSpeed, sprites)
    }
}