package net.watersfall.thuwumcraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class GrindingRecipe extends ShapelessRecipe
{
	private final DefaultedList<Ingredient> inputs;
	private final ItemStack output;
	
	public GrindingRecipe(Identifier id, DefaultedList<Ingredient> inputs, ItemStack output)
	{
		super(id, "grinding", output, inputs);
		this.inputs = inputs;
		this.output = output;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ThuwumcraftRecipes.GRINDING_RECIPE_SERIALIZER;
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory)
	{
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
		for(int i = 0; i < defaultedList.size(); ++i)
		{
			if(inventory.getStack(i).getItem() == Items.BOWL || inventory.getStack(i).getItem() == Items.FLINT)
			{
				defaultedList.set(i, inventory.getStack(i).copy());
			}
		}
		return defaultedList;
	}

	public static class Serializer implements RecipeSerializer<GrindingRecipe>
	{
		private final GrindingRecipe.Serializer.RecipeFactory<GrindingRecipe> recipeFactory;
		public Serializer(GrindingRecipe.Serializer.RecipeFactory<GrindingRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public GrindingRecipe read(Identifier id, JsonObject json)
		{
			DefaultedList<Ingredient> inputs = DefaultedList.of();
			JsonArray jsonInputs = json.getAsJsonArray("inputs");
			for(int i = 0; i < jsonInputs.size(); i++)
			{
				inputs.add(Ingredient.fromJson(jsonInputs.get(i)));
			}
			inputs.add(Ingredient.ofItems(Items.BOWL));
			inputs.add(Ingredient.ofItems(Items.FLINT));
			ItemStack output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get("output").getAsString())));
			return new GrindingRecipe(id, inputs, output);
		}

		@Override
		public GrindingRecipe read(Identifier id, PacketByteBuf buf)
		{
			DefaultedList<Ingredient> inputs = DefaultedList.of();
			int size = buf.readInt();
			for(int i = 0; i < size; i++)
			{
				inputs.add(Ingredient.fromPacket(buf));
			}
			inputs.add(Ingredient.ofItems(Items.BOWL));
			inputs.add(Ingredient.ofItems(Items.FLINT));
			ItemStack output = buf.readItemStack();
			return new GrindingRecipe(id, inputs, output);
		}

		@Override
		public void write(PacketByteBuf buf, GrindingRecipe recipe)
		{
			buf.writeInt(recipe.inputs.size() - 2);
			for(int i = 0; i < recipe.inputs.size() - 2; i++)
			{
				recipe.inputs.get(i).write(buf);
			}
			buf.writeItemStack(recipe.output);
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, DefaultedList<Ingredient> inputs, ItemStack output);
		}
	}
}
