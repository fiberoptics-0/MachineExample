package dev.fiberoptics.machineexample.recipe.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ModContainer extends SimpleContainer {

    private NonNullList<Integer> energyStorage = NonNullList.withSize(1, 0);

    public int getEnergy(int slot) {
        return energyStorage.get(slot);
    }

    public void setEnergy(int slot, int energy) {
        energyStorage.set(slot, energy);
    }

    private NonNullList<FluidStack> fluidStacks = NonNullList.withSize(1, FluidStack.EMPTY);

    public ModContainer() {
        super(1);
    }

    public FluidStack getFluidStack(int slot) {
        return fluidStacks.get(slot);
    }

    public void setFluidStack(int slot, FluidStack fluidStack) {
        fluidStacks.set(slot, fluidStack);
    }
}
