package dev.fiberoptics.machineexample.item;

import dev.fiberoptics.machineexample.MachineExample;
import dev.fiberoptics.machineexample.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MachineExample.MODID);

    public static RegistryObject<BlockItem> MACHINE_ITEM =
            ITEMS.register("machine",() -> new BlockItem(ModBlocks.MACHINE_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
