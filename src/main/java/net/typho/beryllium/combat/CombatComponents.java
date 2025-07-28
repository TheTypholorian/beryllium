package net.typho.beryllium.combat;

import net.minecraft.entity.projectile.TridentEntity;
import net.typho.beryllium.Beryllium;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class CombatComponents implements EntityComponentInitializer {
    public static final ComponentKey<ReelingComponent> REELING = ComponentRegistryV3.INSTANCE.getOrCreate(Beryllium.COMBAT.id("reeling"), ReelingComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(TridentEntity.class, REELING, ReelingComponent::new);
    }
}
