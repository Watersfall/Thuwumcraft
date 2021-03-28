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

public class ResearchUnlockedShapelessRecipe implements ResearchRequiredCraftingRecipe, Recipe<CraftingInventory>
{
	private final Identifier id;
	private final List<Identifier> research;
	private final ShapelessRecipe recipe;

	public ResearchUnlockedShapelessRecipe(Identifier id, ShapelessRecipe recipe, List<Identifier> research)
	{
		this.id = id;
		this.research = research;
		this.recipe = recipe;
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world)
	{
		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inv)
	{
		return this.recipe.craft(inv);
	}

	@Override
	public boolean fits(int width, int height)
	{
		return this.recipe.fits(width, height);
	}

	@Override
	public ItemStack getOutput()
	{
		return this.recipe.getOutput();
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world, PlayerResearchAbility ability)
	{
		if(!recipe.matches(craftingInventory, world))
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
	public Identifier getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyRecipes.RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyRecipes.RESEARCH_UNLOCKED_SHAPELESS_RECIPE;
	}

	public static class Serializer implements RecipeSerializer<ResearchUnlockedShapelessRecipe>
	{
		@Override
		public ResearchUnlockedShapelessRecipe read(Identifier id, JsonObject json)
		{
			ShapelessRecipe recipe = RecipeSerializer.SHAPELESS.read(id, json);
			List<Identifier> research = new ArrayList<>();
			JsonHelper.getArray(json, "research", new JsonArray()).forEach(element -> research.add(Identifier.tryParse(element.getAsString())));
			return new ResearchUnlockedShapelessRecipe(id, recipe, research);
		}

		@Override
		public ResearchUnlockedShapelessRecipe read(Identifier id, PacketByteBuf buf)
		{
			ShapelessRecipe recipe = RecipeSerializer.SHAPELESS.read(id, buf);
			List<Identifier> research = new ArrayList<>();
			int size = buf.readInt();
			for(int i = 0; i < size; i++)
			{
				research.add(buf.readIdentifier());
			}
			return new ResearchUnlockedShapelessRecipe(id, recipe, research);
		}

		@Override
		public void write(PacketByteBuf buf, ResearchUnlockedShapelessRecipe recipe)
		{
			RecipeSerializer.SHAPELESS.write(buf, recipe.recipe);
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
