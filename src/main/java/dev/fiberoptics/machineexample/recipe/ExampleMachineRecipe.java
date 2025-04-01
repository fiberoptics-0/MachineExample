package dev.fiberoptics.machineexample.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import org.jetbrains.annotations.Nullable;

public class ExampleMachineRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final ResourceLocation id;

    public ExampleMachineRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation id) {
        this.ingredients = ingredients;
        this.result = result;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer inv, Level level) {
        if(level.isClientSide()) return false;
        boolean condition1 = ingredients.get(0).test(inv.getItem(0)) &&
                ingredients.get(1).test(inv.getItem(1));
        boolean condition2 = ingredients.get(0).test(inv.getItem(1)) &&
                ingredients.get(1).test(inv.getItem(0));
        return condition1 || condition2;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.copy();
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
            JsonObject outputJson = GsonHelper.getAsJsonObject(jsonObject, "output");
            ItemStack output = CraftingHelper.getItemStack(outputJson, true,true);
            JsonArray ingredientsJson = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
            for (int i = 0; i < ingredientsJson.size(); ++i) {
                ingredients.set(i,Ingredient.fromJson(ingredientsJson.get(i)));
            }
            return new ExampleMachineRecipe(ingredients, output, resourceLocation);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ExampleMachineRecipe exampleMachineRecipe) {
            friendlyByteBuf.writeVarInt(exampleMachineRecipe.ingredients.size());
            for (Ingredient ingredient : exampleMachineRecipe.ingredients) {
                ingredient.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItemStack(exampleMachineRecipe.getResultItem(null),false);
        }

        @Override
        public @Nullable ExampleMachineRecipe fromNetwork(ResourceLocation resourceLocation,
                                                          FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); ++i) {
                ingredients.set(i,Ingredient.fromNetwork(friendlyByteBuf));
            }
            ItemStack output = friendlyByteBuf.readItem();
            return new ExampleMachineRecipe(ingredients, output, resourceLocation);
        }
    }
}
