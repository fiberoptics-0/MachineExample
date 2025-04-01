package dev.fiberoptics.machineexample.block.entity;

import dev.fiberoptics.machineexample.capability.ModItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExampleMachineBlockEntity extends BlockEntity {

    private final ModItemStackHandler inventory = new ModItemStackHandler();
    private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
    private static final int MAX_PROGRESS = 60;
    private int progress = 0;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCap.cast();
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
        if(inventory.getStackInSlot(0).is(Items.IRON_INGOT) && inventory.getStackInSlot(1).is(Items.GOLD_NUGGET)) {
            if(progress >= MAX_PROGRESS) {
                ItemStack stack = inventory.getStackInSlot(2);
                if(stack.isEmpty() || (stack.is(Items.DIAMOND) && stack.getCount() + 1 <= stack.getMaxStackSize())) {
                    inventory.getStackInSlot(0).shrink(1);
                    inventory.getStackInSlot(1).shrink(1);
                    inventory.setStackInSlot(2, new ItemStack(Items.DIAMOND,stack.getCount() + 1));
                    progress = 0;
                }
            } else {
                progress++;
            }
        }
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, ExampleMachineBlockEntity blockEntity) {
        if(level.isClientSide()) return;
        blockEntity.tick(level, pos, state);
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
