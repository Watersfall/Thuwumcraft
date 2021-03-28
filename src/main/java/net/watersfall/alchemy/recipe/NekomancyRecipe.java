package net.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.inventory.NekomancerTableInventory;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.CrystalItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NekomancyRecipe implements Recipe<NekomancerTableInventory>
{
	private final Identifier id;
	private final ShapedRecipe recipe;
	private final List<ItemStack> crystals;
	private final ItemStack output;

	public NekomancyRecipe(Identifier id, ShapedRecipe recipe, List<ItemStack> crystals, ItemStack output)
	{
		this.id = id;
		this.recipe = recipe;
		this.crystals = crystals;
		this.output = output;
	}

	@Override
	public boolean matches(NekomancerTableInventory inv, World world)
	{
		CraftingInventory inventory = new CraftingInventory(null, 3, 3);
		for(int i = 0; i < inv.getCraftingSlots().length; i++)
		{
			inventory.stacks.set(i, inv.getStack(inv.getCraftingSlots()[i]));
		}
		if(recipe.matches(inventory, world))
		{
			for(int i = 0; i < crystals.size(); i++)
			{
				Aspect aspect = ((CrystalItem)crystals.get(i).getItem()).getAspect();
				if(inv.getCrystalSlot(aspect).isEmpty() || inv.getCrystalSlot(aspect).getCount() < crystals.get(i).getCount())
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack craft(NekomancerTableInventory inv)
	{
		for(int i = 0; i < inv.getCraftingSlots().length; i++)
		{
			inv.getStack(inv.getCraftingSlots()[i]).decrement(1);
		}
		for(int i = 0; i < this.crystals.size(); i++)
		{
			inv.getCrystalSlot(((CrystalItem)this.crystals.get(i).getItem()).getAspect()).decrement(this.crystals.get(i).getCount());
		}
		return this.output.copy();
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return this.output;
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyRecipes.NEKOMANCY_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyRecipes.NEKOMANCY_RECIPE;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	public static class Serializer implements RecipeSerializer<NekomancyRecipe>
	{

		@Override
		public NekomancyRecipe read(Identifier id, JsonObject json)
		{
			ShapedRecipe recipe = (ShapedRecipe)Registry.RECIPE_SERIALIZER.get(new Identifier("crafting_shaped")).read(id, json);
			List<ItemStack> list = new ArrayList<>();
			JsonArray array = json.getAsJsonArray("shards");
			for(int i = 0; i < array.size(); i++)
			{
				JsonObject object = array.get(i).getAsJsonObject();
				ItemStack stack = new ItemStack(
						Aspects.ASPECT_TO_CRYSTAL.get(Aspects.getAspectById(Identifier.tryParse(object.get("aspect").getAsString()))),
						JsonHelper.getInt(object, "count", 1)
				);
				list.add(stack);
			}
			return new NekomancyRecipe(id, recipe, list, recipe.getOutput());
		}

		@Override
		public NekomancyRecipe read(Identifier id, PacketByteBuf buf)
		{
			ShapedRecipe recipe = RecipeSerializer.SHAPED.read(id, buf);
			int size = buf.readInt();
			List<ItemStack> shards = new ArrayList<>();
			for(int i = 0; i < size; i++)
			{
				shards.add(buf.readItemStack());
			}
			return new NekomancyRecipe(id, recipe, shards, recipe.getOutput());
		}

		@Override
		public void write(PacketByteBuf buf, NekomancyRecipe recipe)
		{
			RecipeSerializer.SHAPED.write(buf, recipe.recipe);
			buf.writeInt(recipe.crystals.size());
			for(int i = 0; i < recipe.crystals.size(); i++)
			{
				buf.writeItemStack(recipe.crystals.get(i));
			}
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, ShapedRecipe recipe, List<ItemStack> crystals, ItemStack output);
		}
	}
}
