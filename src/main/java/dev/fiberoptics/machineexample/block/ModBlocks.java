package dev.fiberoptics.machineexample.block;

import dev.fiberoptics.machineexample.MachineExample;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MachineExample.MODID);

    public static final RegistryObject<Block> MACHINE_BLOCK =
            BLOCKS.register("machine",ExampleMachineBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
