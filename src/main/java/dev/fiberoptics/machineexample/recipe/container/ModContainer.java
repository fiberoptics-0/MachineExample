package dev.fiberoptics.machineexample.recipe.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraftforge.fluids.FluidStack;

public class ModContainer extends SimpleContainer {

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
