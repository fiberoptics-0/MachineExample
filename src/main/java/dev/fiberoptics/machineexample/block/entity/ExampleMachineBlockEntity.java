package dev.fiberoptics.machineexample.block.entity;

import dev.fiberoptics.machineexample.capability.ModItemStackHandler;
import dev.fiberoptics.machineexample.recipe.ExampleMachineRecipe;
import dev.fiberoptics.machineexample.recipe.ModRecipeTypes;
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
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
        ItemStack result = this.getResult();
        if(!getResult().isEmpty()) {
            if(progress >= MAX_PROGRESS) {
                ItemStack stack = inventory.getStackInSlot(2);
                if(stack.isEmpty() || (stack.is(result.getItem()) && stack.getCount() + 1 <= stack.getMaxStackSize())) {
                    inventory.getStackInSlot(0).shrink(1);
                    inventory.getStackInSlot(1).shrink(1);
                    inventory.setStackInSlot(2, new ItemStack(result.getItem(),stack.getCount() + 1));
                    progress = 0;
                }
            } else {
                progress++;
            }
        }
    }

    private ItemStack getResult() {
        SimpleContainer inv = new SimpleContainer(2);
        inv.setItem(0, inventory.getStackInSlot(0));
        inv.setItem(1, inventory.getStackInSlot(1));
        Optional<ExampleMachineRecipe> recipe =
                this.level.getRecipeManager().getRecipeFor(ExampleMachineRecipe.Type.INSTANCE,inv,this.level);
        if(recipe.isPresent()) {
            return recipe.get().getResultItem(null);
        } else {
            return ItemStack.EMPTY;
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
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        progress = tag.getInt("progress");
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        super.load(tag);
    }

    public ExampleMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MACHINE_BE.get(), pos, blockState);
    }
}
