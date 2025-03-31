package dev.fiberoptics.machineexample.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxUI;

public class ExampleMachineBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == Direction.UP || side == Direction.WEST || side == Direction.DOWN) {
                return inventoryCap.cast();
            }
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

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory",inventory.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        inventory.deserializeNBT(tag.getCompound("inventory"));
        super.load(tag);
    }

    public ExampleMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MACHINE_BE.get(), pos, blockState);
    }
}
