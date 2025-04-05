package dev.fiberoptics.machineexample.block.entity;

import dev.fiberoptics.machineexample.capability.ModEnergyStorage;
import dev.fiberoptics.machineexample.capability.ModFluidStackHandler;
import dev.fiberoptics.machineexample.capability.ModItemStackHandler;
import dev.fiberoptics.machineexample.menu.ExampleMachineMenu;
import dev.fiberoptics.machineexample.menu.ModMenuTypes;
import dev.fiberoptics.machineexample.recipe.ExampleMachineRecipe;
import dev.fiberoptics.machineexample.recipe.container.ModContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ExampleMachineBlockEntity extends BlockEntity implements MenuProvider {

    private final ModItemStackHandler inventory = new ModItemStackHandler();
    private final ModFluidStackHandler fluidInventory = new ModFluidStackHandler();
    private final ModEnergyStorage energyStorage = new ModEnergyStorage();
    private final LazyOptional<IFluidHandler> fluidInventoryCap = LazyOptional.of(() -> fluidInventory);
    private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
    private final LazyOptional<IEnergyStorage> energyStorageCap = LazyOptional.of(() -> energyStorage);
    private static final int MAX_PROGRESS = 60;
    private int progress = 0;
    public final ContainerData data;
    private int scaledInputFluidHeight = 0;
    private int scaledOutputFluidHeight = 0;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCap.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidInventoryCap.cast();
        }
        if(cap == ForgeCapabilities.ENERGY) {
            return energyStorageCap.cast();
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
        Optional<ExampleMachineRecipe> recipe = this.getRecipe();
        if(scaledInputFluidHeight != getScaledHeight(0) || scaledOutputFluidHeight != getScaledHeight(1)) {
            scaledInputFluidHeight = getScaledHeight(0);
            scaledOutputFluidHeight = getScaledHeight(1);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
        if(recipe.isPresent()) {
            if(progress >= MAX_PROGRESS) {
                ItemStack resultItem = recipe.get().getResultItem(null);
                FluidStack resultFluid = recipe.get().getOutputFluid();
                ItemStack itemStack = inventory.getStackInSlot(1);
                FluidStack fluidStack = fluidInventory.getFluidInTank(1);
                boolean itemCondition = (itemStack.isEmpty() || (itemStack.is(resultItem.getItem()) &&
                        itemStack.getCount()+resultItem.getCount() <= itemStack.getMaxStackSize()));
                boolean fluidCondition = (fluidStack.isEmpty() || (fluidStack.isFluidEqual(resultFluid) &&
                        fluidStack.getAmount()+resultFluid.getAmount() <= fluidInventory.getTankCapacity(1)));
                if(itemCondition && fluidCondition) {
                    fluidInventory.getFluidInTank(0).shrink(recipe.get().getFluidCost());
                    inventory.getStackInSlot(0).shrink(1);
                    energyStorage.setEnergy(energyStorage.getEnergyStored() - recipe.get().getEnergyCost());
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
        } else progress = 0;
    }

    public FluidStack getFluidStack(int i) {
        return fluidInventory.getFluidInTank(i);
    }

    public int getCapacity(int i) {
        return fluidInventory.getTankCapacity(i);
    }

    private Optional<ExampleMachineRecipe> getRecipe() {
        ModContainer inv = new ModContainer();
        inv.setItem(0, inventory.getStackInSlot(0));
        inv.setFluidStack(0, fluidInventory.getFluidInTank(0));
        inv.setEnergy(0, energyStorage.getEnergyStored());
        return this.level.getRecipeManager().getRecipeFor(ExampleMachineRecipe.Type.INSTANCE,inv,this.level);
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
        tag.putInt("Energy", energyStorage.getEnergyStored());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("progress")) {
            progress = tag.getInt("progress");
        }
        if(tag.contains("Inventory")) {
            inventory.deserializeNBT(tag.getCompound("Inventory"));
        }
        if(tag.contains("FluidInventory")) {
            fluidInventory.deserializeNBT(tag.getCompound("FluidInventory"));
        }
        if(tag.contains("Energy")) {
            energyStorage.setEnergy(tag.getInt("Energy"));
        }
        super.load(tag);
    }

    public ExampleMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MACHINE_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> MAX_PROGRESS;
                    case 2 -> energyStorage.getEnergyStored();
                    case 3 -> energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    private int getScaledHeight(int index) {
        return (int)(41*(((float)fluidInventory.getFluidInTank(index).getAmount())
                /((float)fluidInventory.getTankCapacity(index))));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("FluidInventory",fluidInventory.serializeNBT());
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Example Machine");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ExampleMachineMenu(ModMenuTypes.EXAMPLE_MACHINE.get(),containerId,inventory,this,this.data);
    }
}
