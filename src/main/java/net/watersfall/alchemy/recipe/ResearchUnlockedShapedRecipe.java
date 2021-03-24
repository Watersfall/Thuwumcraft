package net.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.watersfall.alchemy.accessor.ShapedRecipeAccessor;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResearchUnlockedShapedRecipe extends ShapedRecipe implements ResearchRequiredCraftingRecipe
{
	private final List<Identifier> research;

	public ResearchUnlockedShapedRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> ingredients, ItemStack output, List<Identifier> research)
	{
		super(id, group, width, height, ingredients, output);
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
		return AlchemyRecipes.RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<ResearchUnlockedShapedRecipe>
	{
		@Override
		public ResearchUnlockedShapedRecipe read(Identifier id, JsonObject json)
		{
			String group = JsonHelper.getString(json, "group", "");
			Map<String, Ingredient> components = ShapedRecipe.getComponents(JsonHelper.getObject(json, "key"));
			String[] pattern = ShapedRecipe.combinePattern(ShapedRecipe.getPattern(JsonHelper.getArray(json, "pattern")));
			int width = pattern[0].length();
			int height = pattern.length;
			DefaultedList<Ingredient> ingredients = ShapedRecipe.getIngredients(pattern, components, width, height);
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			List<Identifier> research = new ArrayList<>();
			JsonHelper.getArray(json, "research", new JsonArray()).forEach(element -> research.add(Identifier.tryParse(element.getAsString())));
			return new ResearchUnlockedShapedRecipe(id, group, width, height, ingredients, output, research);
		}

		@Override
		public ResearchUnlockedShapedRecipe read(Identifier id, PacketByteBuf buf)
		{
			int width = buf.readVarInt();
			int height = buf.readVarInt();
			String group = buf.readString();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
			for(int i = 0; i < ingredients.size(); i++)
			{
				ingredients.set(i, Ingredient.fromPacket(buf));
			}
			ItemStack output = buf.readItemStack();
			int size = buf.readInt();
			List<Identifier> research = new ArrayList<>();
			for(int i = 0; i < size; i++)
			{
				research.add(buf.readIdentifier());
			}
			return new ResearchUnlockedShapedRecipe(id, group, width, height, ingredients, output, research);
		}

		@Override
		public void write(PacketByteBuf buf, ResearchUnlockedShapedRecipe recipe)
		{
			ShapedRecipeAccessor accessor = (ShapedRecipeAccessor)recipe;
			buf.writeVarInt(accessor.getWidth());
			buf.writeVarInt(accessor.getHeight());
			buf.writeString(accessor.getGroup());
			for(Ingredient ingredient : accessor.getInputs())
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
			T create(Identifier id, String group, int width, int height, DefaultedList<Ingredient> ingredients, ItemStack output, List<Identifier> research);
		}
	}
}
