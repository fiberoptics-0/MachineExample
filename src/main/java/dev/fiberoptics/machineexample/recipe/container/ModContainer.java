package dev.fiberoptics.machineexample.recipe.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraftforge.fluids.FluidStack;

public class ModContainer extends SimpleContainer {

    NonNullList<FluidStack> fluids = NonNullList.withSize(2, FluidStack.EMPTY);

    public ModContainer() {
        super(2);
    }


}
