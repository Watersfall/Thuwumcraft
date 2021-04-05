package net.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.inventory.AspectCraftingInventory;
import net.watersfall.alchemy.item.CrystalItem;

import java.util.ArrayList;
import java.util.List;

public class AspectCraftingShapedRecipe implements CraftingRecipe
{
	protected final ResearchUnlockedShapedRecipe recipe;
	protected final List<ItemStack> aspects;
	protected final Identifier id;
	protected final int vis;

	public AspectCraftingShapedRecipe(Identifier id, ResearchUnlockedShapedRecipe recipe, List<ItemStack> aspects, int vis)
	{
		this.id = id;
		this.recipe = recipe;
		this.aspects = aspects;
		this.vis = vis;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world)
	{
		if(!recipe.matches(inv, world))
		{
			return false;
		}
		else
		{
			AspectCraftingInventory inventory = (AspectCraftingInventory)inv;
			for(ItemStack aspect : aspects)
			{
				CrystalItem item = (CrystalItem)aspect.getItem();
				if(inventory.getStack(item.getAspect()).getCount() < aspect.getCount())
				{
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack craft(CraftingInventory inv)
	{
		return this.getOutput().copy();
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
	public Identifier getId()
	{
		return recipe.getId();
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyRecipes.ASPECT_SHAPED_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeType.CRAFTING;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	public List<ItemStack> getAspects()
	{
		return this.aspects;
	}

	public int getVis()
	{
		return this.vis;
	}

	public static class Serializer implements RecipeSerializer<AspectCraftingShapedRecipe>
	{
		@Override
		public AspectCraftingShapedRecipe read(Identifier id, JsonObject json)
		{
			ResearchUnlockedShapedRecipe recipe = AlchemyRecipes.RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER.read(id, json);
			JsonArray array = JsonHelper.getArray(json, "crystals");
			List<ItemStack> list = new ArrayList<>();
			for(int i = 0; i < array.size(); i++)
			{
				JsonObject object = array.get(i).getAsJsonObject();
				Item crystal = Aspects.ASPECT_TO_CRYSTAL.get(Aspects.getAspectById(new Identifier(object.get("aspect").getAsString())));
				int count = object.get("count").getAsInt();
				list.add(new ItemStack(crystal, count));
			}
			int vis = json.get("vis").getAsInt();
			return new AspectCraftingShapedRecipe(id, recipe, list, vis);
		}

		@Override
		public AspectCraftingShapedRecipe read(Identifier id, PacketByteBuf buf)
		{
			ResearchUnlockedShapedRecipe recipe = AlchemyRecipes.RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER.read(id, buf);
			int size = buf.readInt();
			List<ItemStack> list = new ArrayList<>();
			for(int i = 0; i < size; i++)
			{
				list.add(buf.readItemStack());
			}
			int vis = buf.readInt();
			return new AspectCraftingShapedRecipe(id, recipe, list, vis);
		}

		@Override
		public void write(PacketByteBuf buf, AspectCraftingShapedRecipe recipe)
		{
			AlchemyRecipes.RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER.write(buf, recipe.recipe);
			buf.writeInt(recipe.aspects.size());
			for(int i = 0; i < recipe.aspects.size(); i++)
			{
				buf.writeItemStack(recipe.aspects.get(i));
			}
			buf.writeInt(recipe.vis);
		}
	}
}
