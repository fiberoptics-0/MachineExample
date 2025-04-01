package dev.fiberoptics.machineexample.block.entity;

import dev.fiberoptics.machineexample.MachineExample;
import dev.fiberoptics.machineexample.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MachineExample.MODID);

    public static final RegistryObject<BlockEntityType<ExampleMachineBlockEntity>> MACHINE_BE =
            BLOCK_ENTITIES.register("machine_be", () -> BlockEntityType.Builder
                    .of(ExampleMachineBlockEntity::new, ModBlocks.MACHINE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
