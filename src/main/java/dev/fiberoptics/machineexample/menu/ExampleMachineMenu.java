package dev.fiberoptics.machineexample.menu;

import dev.fiberoptics.machineexample.block.ModBlocks;
import dev.fiberoptics.machineexample.block.entity.ExampleMachineBlockEntity;
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
            this.addSlot(new SlotItemHandler(handler,0,55,34));
            this.addSlot(new SlotItemHandler(handler,1,112,34));
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
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(
                level,blockEntity.getBlockPos()), player,ModBlocks.MACHINE_BLOCK.get());
    }
}
