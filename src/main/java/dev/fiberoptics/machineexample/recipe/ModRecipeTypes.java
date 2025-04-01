package dev.fiberoptics.machineexample.recipe;

import dev.fiberoptics.machineexample.MachineExample;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public enum ModRecipeTypes {
    EXAMPLE_MACHINE("example_machine",ExampleMachineRecipe.Type.INSTANCE,ExampleMachineRecipe.Serializer.INSTANCE);

    private RegistryObject<RecipeSerializer<?>> recipeSerializer;
    private RegistryObject<RecipeType<?>> recipeType;

    ModRecipeTypes(String name, RecipeType<?> recipeType,
                   RecipeSerializer<?> recipeSerializer) {

        this.recipeType = Registers.RECIPE_TYPES.register(name,() -> recipeType);
        this.recipeSerializer = Registers.RECIPE_SERIALIZERS.register(name,() -> recipeSerializer);
    }

    public RegistryObject<RecipeSerializer<?>> getRecipeSerializer() {
        return recipeSerializer;
    }

    public RegistryObject<RecipeType<?>> getRecipeType() {
        return recipeType;
    }

    public static void register(IEventBus bus) {
        Registers.RECIPE_TYPES.register(bus);
        Registers.RECIPE_SERIALIZERS.register(bus);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
                DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MachineExample.MODID);
        private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
                DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MachineExample.MODID);
    }
}
