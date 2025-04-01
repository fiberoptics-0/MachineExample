package dev.fiberoptics.machineexample.block.entity;

import dev.fiberoptics.machineexample.capability.ModFluidStackHandler;
import dev.fiberoptics.machineexample.capability.ModItemStackHandler;
import dev.fiberoptics.machineexample.recipe.ExampleMachineRecipe;
import dev.fiberoptics.machineexample.recipe.ModRecipeTypes;
import dev.fiberoptics.machineexample.recipe.container.ModContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.Optional;

public class ExampleMachineBlockEntity extends BlockEntity {

    private final ModItemStackHandler inventory = new ModItemStackHandler();
    private final ModFluidStackHandler fluidInventory = new ModFluidStackHandler();
    private final LazyOptional<IFluidHandler> fluidInventoryCap = LazyOptional.of(() -> fluidInventory);
    private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
    private static final int MAX_PROGRESS = 60;
    private int progress = 0;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCap.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidInventoryCap.cast();
        }
        return super.getCapability(cap, side);
    }

    public void drop() {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    private void tick(Level level, BlockPos pos, BlockState state) {
        Pair<FluidStack,ItemStack> result = this.getResult();
        if(!result.getA().isEmpty() || !result.getB().isEmpty()) {
            if(progress >= MAX_PROGRESS) {
                ItemStack resultItem = result.getB();
                FluidStack resultFluid = result.getA();
                ItemStack itemStack = inventory.getStackInSlot(1);
                FluidStack fluidStack = fluidInventory.getFluidInTank(1);
                boolean itemCondition = (itemStack.isEmpty() || (itemStack.is(resultItem.getItem()) &&
                        itemStack.getCount()+resultItem.getCount() <= itemStack.getMaxStackSize()));
                boolean fluidCondition = (fluidStack.isEmpty() || (fluidStack.isFluidEqual(resultFluid) &&
                        fluidStack.getAmount()+resultFluid.getAmount() <= fluidInventory.getTankCapacity(1)));
                if(itemCondition && fluidCondition) {
                    fluidInventory.getFluidInTank(0).shrink(this.getFluidCost());
                    inventory.getStackInSlot(0).shrink(1);
                    inventory.setStackInSlot(1,
                            new ItemStack(resultItem.getItem(), itemStack.getCount()+resultItem.getCount()));
                    fluidInventory.setFluidInTank(1,
                            new FluidStack(resultFluid.getFluid(),
                                    resultFluid.getAmount()+fluidStack.getAmount()));
                    progress = 0;
                }
            } else {
                progress++;
            }
        }
    }

    private Optional<ExampleMachineRecipe> getRecipe() {
        ModContainer inv = new ModContainer();
        inv.setItem(0, inventory.getStackInSlot(0));
        inv.setFluidStack(0, fluidInventory.getFluidInTank(0));
        return this.level.getRecipeManager().getRecipeFor(ExampleMachineRecipe.Type.INSTANCE,inv,this.level);
    }

    private Pair<FluidStack,ItemStack> getResult() {
        Optional<ExampleMachineRecipe> recipe = this.getRecipe();
        if(recipe.isPresent()) {
            return new Pair<>(recipe.get().getOutputFluid(), recipe.get().getResultItem(null));
        } else {
            return new Pair<>(FluidStack.EMPTY, ItemStack.EMPTY);
        }
    }

    private int getFluidCost() {
        Optional<ExampleMachineRecipe> recipe = this.getRecipe();
        if(recipe.isPresent()) {
            return recipe.get().getFluidCost();
        } else {
            return 0;
        }
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, ExampleMachineBlockEntity blockEntity) {
        if(level.isClientSide()) return;
        blockEntity.tick(level, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("progress", progress);
        tag.put("Inventory",inventory.serializeNBT());
        tag.put("FluidInventory",fluidInventory.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        progress = tag.getInt("progress");
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        fluidInventory.deserializeNBT(tag.getCompound("FluidInventory"));
        super.load(tag);
    }

    public ExampleMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MACHINE_BE.get(), pos, blockState);
    }
}
