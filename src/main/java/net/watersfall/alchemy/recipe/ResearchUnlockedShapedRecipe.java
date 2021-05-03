package net.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

import java.util.ArrayList;
import java.util.List;

public class ResearchUnlockedShapedRecipe implements ResearchRequiredCraftingRecipe, CraftingRecipe
{
	private final Identifier id;
	private final List<Identifier> research;
	public final ShapedRecipe recipe;

	public ResearchUnlockedShapedRecipe(Identifier id, ShapedRecipe recipe, List<Identifier> research)
	{
		this.id = id;
		this.recipe = recipe;
		this.research = research;
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world)
	{
		return recipe.matches(craftingInventory, world);
	}

	@Override
	public ItemStack craft(CraftingInventory inv)
	{
		return recipe.craft(inv);
	}

	@Override
	public boolean fits(int width, int height)
	{
		return recipe.fits(width, height);
	}

	@Override
	public ItemStack getOutput()
	{
		return recipe.getOutput();
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
	public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory)
	{
		return recipe.getRemainder(inventory);
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
		return AlchemyRecipes.RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeType.CRAFTING;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients()
	{
		return this.recipe.getIngredients();
	}

	public static class Serializer implements RecipeSerializer<ResearchUnlockedShapedRecipe>
	{
		@Override
		public ResearchUnlockedShapedRecipe read(Identifier id, JsonObject json)
		{
			ShapedRecipe recipe = RecipeSerializer.SHAPED.read(id, json);
			List<Identifier> research = new ArrayList<>();
			JsonHelper.getArray(json, "research", new JsonArray()).forEach(element -> research.add(Identifier.tryParse(element.getAsString())));
			return new ResearchUnlockedShapedRecipe(id, recipe, research);
		}

		@Override
		public ResearchUnlockedShapedRecipe read(Identifier id, PacketByteBuf buf)
		{
			ShapedRecipe recipe = RecipeSerializer.SHAPED.read(id, buf);
			int size = buf.readInt();
			List<Identifier> research = new ArrayList<>();
			for(int i = 0; i < size; i++)
			{
				research.add(buf.readIdentifier());
			}
			return new ResearchUnlockedShapedRecipe(id, recipe, research);
		}

		@Override
		public void write(PacketByteBuf buf, ResearchUnlockedShapedRecipe recipe)
		{
			RecipeSerializer.SHAPED.write(buf, recipe.recipe);
			buf.writeInt(recipe.research.size());
			for(int i = 0; i < recipe.research.size(); i++)
			{
				buf.writeIdentifier(recipe.research.get(i));
			}
		}
	}
}
