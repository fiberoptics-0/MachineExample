package dev.fiberoptics.machineexample.capability;

import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {
    public ModEnergyStorage() {
        super(10000);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
