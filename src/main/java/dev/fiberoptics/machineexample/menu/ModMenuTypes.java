package dev.fiberoptics.machineexample.menu;

import dev.fiberoptics.machineexample.MachineExample;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MachineExample.MODID);

    public static final RegistryObject<MenuType<ExampleMachineMenu>> EXAMPLE_MACHINE =
            MENU_TYPES.register("example_machine",() -> IForgeMenuType.create(ExampleMachineMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
