package dev.fiberoptics.machineexample.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class ModFluidStackHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

    private NonNullList<FluidStack> fluidStacks = NonNullList.withSize(2, FluidStack.EMPTY);

    public void setFluidInTank(int i, FluidStack fluidStack) {
        fluidStacks.set(i, fluidStack);
    }

    @Override
    public int getTanks() {
        return fluidStacks.size();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return fluidStacks.get(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return 10000;
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack fluidInTank = fluidStacks.get(0);
        int amount;
        int transferAmount;
        if(fluidInTank.isEmpty()) {
            amount = Math.min(fluidStack.getAmount(),this.getTankCapacity(0));
            transferAmount = amount;
        } else if (fluidInTank.isFluidEqual(fluidStack)) {
            amount = Math.min(fluidInTank.getAmount()+fluidStack.getAmount(),this.getTankCapacity(0));
            transferAmount = amount - fluidInTank.getAmount();
        } else return 0;

        if(fluidAction.execute()) {
            FluidStack fillStack = fluidStack.copy();
            fillStack.setAmount(amount);
            fluidStacks.set(0, fillStack);
        }
        return transferAmount;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        if(fluidStack.isEmpty() || !fluidStack.isFluidEqual(fluidStacks.get(1))) {
            return FluidStack.EMPTY;
        }
        return drain(fluidStack.getAmount(),fluidAction);
    }

    @Override
    public @NotNull FluidStack drain(int maxAmount, FluidAction fluidAction) {
        FluidStack fluidInTank = fluidStacks.get(1);
        if(fluidInTank.isEmpty()) {
            return FluidStack.EMPTY;
        }
        int transferAmount = Math.min(fluidInTank.getAmount(),maxAmount);
        int amount = fluidInTank.getAmount() - transferAmount;
        if(fluidAction.execute()) {
            FluidStack leftStack = fluidInTank.copy();
            if(amount > 0) {
                leftStack.setAmount(amount);
            } else {
                leftStack = FluidStack.EMPTY;
            }
            fluidStacks.set(1, leftStack);
        }
        FluidStack drainStack = fluidInTank.copy();
        drainStack.setAmount(transferAmount);
        return drainStack;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for(int i = 0; i < fluidStacks.size(); i++) {
            CompoundTag stackTag = new CompoundTag();
            stackTag.putInt("Slot", i);
            fluidStacks.get(i).writeToNBT(stackTag);
            listTag.add(stackTag);
        }
        tag.put("Fluids", listTag);
        tag.putInt("Size", fluidStacks.size());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        fluidStacks = NonNullList.withSize(tag.getInt("Size"), FluidStack.EMPTY);
        ListTag listTag = tag.getList("Fluids", 10);
        for(int i = 0; i < listTag.size(); i++) {
            CompoundTag stackTag = listTag.getCompound(i);
            int slot = stackTag.getInt("Slot");
            if(slot>0 && slot<fluidStacks.size()) {
                fluidStacks.set(slot, FluidStack.loadFluidStackFromNBT(stackTag));
            }
        }
    }
}
