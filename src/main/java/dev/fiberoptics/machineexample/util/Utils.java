package dev.fiberoptics.machineexample.util;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils {
    public static FluidStack getFluidStack(JsonObject json) {
        if(!json.has("fluid")) return FluidStack.EMPTY;
        ResourceLocation fluidName = new ResourceLocation(json.get("fluid").getAsString());
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
        if(fluid == null) return FluidStack.EMPTY;
        if(json.has("amount")) {
            return new FluidStack(fluid, json.get("amount").getAsInt());
        }
        return new FluidStack(fluid,1000);
    }
}
