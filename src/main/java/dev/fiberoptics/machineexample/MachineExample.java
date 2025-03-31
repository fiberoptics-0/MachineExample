package dev.fiberoptics.machineexample;

import dev.fiberoptics.machineexample.block.ModBlocks;
import dev.fiberoptics.machineexample.block.entity.ModBlockEntities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MachineExample.MODID)
public class MachineExample {

    public static final String MODID = "machineexample";

    public MachineExample() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
    }
}
