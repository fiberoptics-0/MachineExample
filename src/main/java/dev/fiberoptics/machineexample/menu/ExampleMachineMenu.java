package dev.fiberoptics.machineexample.menu;

import dev.fiberoptics.machineexample.block.ModBlocks;
import dev.fiberoptics.machineexample.block.entity.ExampleMachineBlockEntity;
import dev.fiberoptics.machineexample.menu.slot.ModSlotItemHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ExampleMachineMenu extends AbstractContainerMenu {

    public final ExampleMachineBlockEntity blockEntity;
    public final Level level;
    public final ContainerData data;

    public ExampleMachineMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(ModMenuTypes.EXAMPLE_MACHINE.get(),containerId,inv,
                inv.player.level().getBlockEntity(extraData.readBlockPos()),new SimpleContainerData(4));
    }

    public ExampleMachineMenu(MenuType<?> menuType, int containerId,
                                 Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(menuType, containerId);
        this.blockEntity = (ExampleMachineBlockEntity) blockEntity;
        this.level = inv.player.level();
        this.data = data;
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new ModSlotItemHandler(handler,0,55,34));
            this.addSlot(new ModSlotItemHandler(handler,1,112,34));
        });
        addDataSlots(data);
    }

    private void addPlayerInventory(Inventory inv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv,j+i*9+9,8+j*18,84+i*18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int i=0; i<9; i++) {
            this.addSlot(new Slot(inv,i,8+i*18,142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if(index < 9 + 27) {
            if(!moveItemStackTo(sourceStack,9 + 27, 9 + 27 + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 9 + 27 + 2) {
            if(!moveItemStackTo(sourceStack,0, 9 + 27, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if(sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(
                level,blockEntity.getBlockPos()), player,ModBlocks.MACHINE_BLOCK.get());
    }
}
