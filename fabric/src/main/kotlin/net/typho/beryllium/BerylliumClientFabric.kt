package net.typho.beryllium

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.typho.beryllium.end_city.GusterParticle

object BerylliumClientFabric : ClientModInitializer {
    override fun onInitializeClient() {
        BerylliumClient.init()
        ParticleFactoryRegistry.getInstance().register(ModParticles.guster.get(), GusterParticle::Provider)
    }
}