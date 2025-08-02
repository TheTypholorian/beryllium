package net.typho.beryllium.combat;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.typho.beryllium.exploring.Exploring;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class ReelingComponent implements ComponentV3, AutoSyncedComponent {
    private float reeling = 0;
    public final TridentEntity trident;

    public ReelingComponent(TridentEntity trident) {
        this.trident = trident;
    }

    public float getReeling() {
        return reeling;
    }

    public void setReeling(float reeling) {
        this.reeling = reeling;
        Exploring.REELING.sync(trident);
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        reeling = nbt.getFloat("reeling");
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putFloat("reeling", reeling);
    }
}
