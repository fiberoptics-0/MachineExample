package dev.fiberoptics.machineexample.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.fiberoptics.machineexample.recipe.container.ModContainer;
import dev.fiberoptics.machineexample.util.Utils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class ExampleMachineRecipe implements Recipe<ModContainer> {

    private final Ingredient inputItem;
    private final FluidStack inputFluid;
    private final ItemStack outputItem;
    private final FluidStack outputFluid;
    private final ResourceLocation id;

    public ExampleMachineRecipe(Ingredient inputItem, FluidStack inputFluid,
                                FluidStack outputFluid,ItemStack outputItem, ResourceLocation id) {
        this.inputItem = inputItem;
        this.inputFluid = inputFluid;
        this.outputItem = outputItem;
        this.outputFluid = outputFluid;
        this.id = id;
    }

    @Override
    public boolean matches(ModContainer inv, Level level) {
        if(level.isClientSide()) return false;
        boolean condition1 = inputItem.test(inv.getItem(0));
        boolean condition2 = inputFluid.isFluidEqual(inv.getFluidStack(0)) &&
                inv.getFluidStack(0).getAmount() >= inputFluid.getAmount();
        return condition1 && condition2;
    }

    public int getFluidCost() {
        return inputFluid.getAmount();
    }

    @Override
    public ItemStack assemble(ModContainer simpleContainer, RegistryAccess registryAccess) {
        return this.outputItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.outputItem.copy();
    }

    public FluidStack getOutputFluid() {
        return this.outputFluid;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ExampleMachineRecipe> {
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<ExampleMachineRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public ExampleMachineRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            JsonObject outputsJson = GsonHelper.getAsJsonObject(jsonObject, "outputs");
            JsonObject inputsJson = GsonHelper.getAsJsonObject(jsonObject, "inputs");
            JsonObject outputItemJson = GsonHelper.getAsJsonObject(outputsJson, "item");
            JsonObject outputFluidJson = GsonHelper.getAsJsonObject(outputsJson, "fluid");
            JsonObject inputItemJson = GsonHelper.getAsJsonObject(inputsJson, "item");
            JsonObject inputFluidJson = GsonHelper.getAsJsonObject(inputsJson, "fluid");
            ItemStack outputItem = CraftingHelper.getItemStack(outputItemJson, true,true);
            FluidStack outputFluid = Utils.getFluidStack(outputFluidJson);
            Ingredient inputItem = Ingredient.fromJson(inputItemJson);
            FluidStack inputFluid = Utils.getFluidStack(inputFluidJson);
            return new ExampleMachineRecipe(inputItem,inputFluid,outputFluid,outputItem,resourceLocation);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ExampleMachineRecipe exampleMachineRecipe) {
            exampleMachineRecipe.inputItem.toNetwork(friendlyByteBuf);
            exampleMachineRecipe.inputFluid.writeToPacket(friendlyByteBuf);
            exampleMachineRecipe.outputFluid.writeToPacket(friendlyByteBuf);
            friendlyByteBuf.writeItemStack(exampleMachineRecipe.outputItem,false);
        }

        @Override
        public @Nullable ExampleMachineRecipe fromNetwork(ResourceLocation resourceLocation,
                                                          FriendlyByteBuf friendlyByteBuf) {
            Ingredient inputItem = Ingredient.fromNetwork(friendlyByteBuf);
            FluidStack inputFluid = FluidStack.readFromPacket(friendlyByteBuf);
            FluidStack outputFluid = FluidStack.readFromPacket(friendlyByteBuf);
            ItemStack outputItem = friendlyByteBuf.readItem();
            return new ExampleMachineRecipe(inputItem,inputFluid,outputFluid,outputItem, resourceLocation);
        }
    }
}
