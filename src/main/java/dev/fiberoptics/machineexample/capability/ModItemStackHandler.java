package dev.fiberoptics.machineexample.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ModItemStackHandler extends ItemStackHandler {
    public ModItemStackHandler() {
        super(2);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if(slot == 1) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(slot != 1) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }
}
