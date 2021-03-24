package net.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.watersfall.alchemy.accessor.ShapelessRecipeAccessor;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResearchUnlockedShapelessRecipe extends ShapelessRecipe implements ResearchRequiredCraftingRecipe
{
	private final List<Identifier> research;

	public ResearchUnlockedShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, List<Identifier> research)
	{
		super(id, group, output, input);
		this.research = research;
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world)
	{
		return false;
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world, PlayerResearchAbility ability)
	{
		if(!super.matches(craftingInventory, world))
		{
			return false;
		}
		else
		{
			for(int i = 0; i < this.research.size(); i++)
			{
				if(!ability.hasResearch(research.get(i)))
				{
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyRecipes.RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<ResearchUnlockedShapelessRecipe>
	{
		@Override
		public ResearchUnlockedShapelessRecipe read(Identifier id, JsonObject json)
		{
			String string = JsonHelper.getString(json, "group", "");
			DefaultedList<Ingredient> defaultedList = ShapelessRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			if (defaultedList.isEmpty())
			{
				throw new JsonParseException("No ingredients for shapeless recipe");
			}
			else if (defaultedList.size() > 9)
			{
				throw new JsonParseException("Too many ingredients for shapeless recipe");
			}
			else
			{
				ItemStack itemStack = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
				List<Identifier> research = new ArrayList<>();
				JsonHelper.getArray(json, "research", new JsonArray()).forEach(element -> research.add(Identifier.tryParse(element.getAsString())));
				return new ResearchUnlockedShapelessRecipe(id, string, itemStack, defaultedList, research);
			}
		}

		@Override
		public ResearchUnlockedShapelessRecipe read(Identifier id, PacketByteBuf buf)
		{
			String string = buf.readString();
			int i = buf.readVarInt();
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
			for(int j = 0; j < defaultedList.size(); ++j)
			{
				defaultedList.set(j, Ingredient.fromPacket(buf));
			}
			ItemStack itemStack = buf.readItemStack();int size = buf.readInt();
			List<Identifier> research = new ArrayList<>();
			for(i = 0; i < size; i++)
			{
				research.add(buf.readIdentifier());
			}
			return new ResearchUnlockedShapelessRecipe(id, string, itemStack, defaultedList, research);
		}

		@Override
		public void write(PacketByteBuf buf, ResearchUnlockedShapelessRecipe recipe)
		{
			ShapelessRecipeAccessor accessor = (ShapelessRecipeAccessor)recipe;
			buf.writeString(accessor.getGroup());
			buf.writeVarInt(accessor.getInput().size());
			for(Ingredient ingredient : accessor.getInput())
			{
				ingredient.write(buf);
			}
			buf.writeItemStack(accessor.getOutput());
			buf.writeInt(recipe.research.size());
			for(int i = 0; i < recipe.research.size(); i++)
			{
				buf.writeIdentifier(recipe.research.get(i));
			}
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, List<Identifier> research);
		}
	}
}
