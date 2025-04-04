package dev.fiberoptics.machineexample;

import dev.fiberoptics.machineexample.block.ModBlocks;
import dev.fiberoptics.machineexample.block.entity.ModBlockEntities;
import dev.fiberoptics.machineexample.item.ModItems;
import dev.fiberoptics.machineexample.menu.ModMenuTypes;
import dev.fiberoptics.machineexample.recipe.ModRecipeTypes;
import dev.fiberoptics.machineexample.screen.ExampleMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(MachineExample.MODID)
public class MachineExample {

    public static final String MODID = "machineexample";

    public MachineExample() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.EXAMPLE_MACHINE.get(), ExampleMachineScreen::new);
    }
}
