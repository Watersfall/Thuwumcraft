package com.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.blockentity.BrewingCauldronEntity;
import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CauldronItemRecipe implements Recipe<BrewingCauldronInventory>
{
	public final Identifier id;
	public final Ingredient catalyst;
	public final int waterUse;
	public final List<Ingredient> inputs;
	public final ItemStack output;

	public CauldronItemRecipe(Identifier id, Ingredient catalyst, int waterUse, List<Ingredient> inputs, ItemStack output)
	{
		this.id = id;
		this.catalyst = catalyst;
		this.waterUse = waterUse;
		this.inputs = inputs;
		this.output = output;
	}

	@Override
	public boolean matches(BrewingCauldronInventory inv, World world)
	{
		if(catalyst.test(inv.getInput().get(0)))
		{
			if(inputs.size() > inv.getIngredientCount())
			{
				return false;
			}
			else
			{
				for(int i = 0; i < inputs.size(); i++)
				{
					boolean hasInput = false;
					for(int o = 0; o < inv.getIngredientCount(); o++)
					{
						if(inputs.get(i).test(inv.getStack(o)))
						{
							hasInput = true;
						}
					}
					if(!hasInput)
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack craft(BrewingCauldronInventory inv)
	{
		byte delete = 0;
		for(int i = 0; i < inputs.size(); i++)
		{
			for(int o = 0; o < inv.getIngredientCount(); o++)
			{
				if(inputs.get(i).test(inv.getStack(o)))
				{
					inv.removeStack(o);
					delete++;
				}
			}
		}
		((BrewingCauldronEntity)inv).setIngredientCount((byte) (inv.getIngredientCount() - delete));
		inv.markDirty();
		return getOutput().copy();
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
		return AlchemyMod.CAULDRON_ITEM_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyMod.CAULDRON_ITEM_RECIPE;
	}

	public static class Serializer implements RecipeSerializer<CauldronItemRecipe>
	{
		private final CauldronItemRecipe.Serializer.RecipeFactory<CauldronItemRecipe> recipeFactory;

		public Serializer(CauldronItemRecipe.Serializer.RecipeFactory<CauldronItemRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public CauldronItemRecipe read(Identifier id, JsonObject json)
		{
			Ingredient catalyst = Ingredient.fromJson(json.get("catalyst"));
			int waterUse = json.get("water_use").getAsInt();
			JsonArray array = json.getAsJsonArray("inputs");
			List<Ingredient> inputs = new ArrayList<>(array.size());
			for(int i = 0; i < array.size(); i++)
			{
				inputs.add(Ingredient.fromJson(array.get(i)));
			}
			ItemStack output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get("output").getAsString())));
			return new CauldronItemRecipe(id, catalyst, waterUse, inputs, output);
		}

		@Override
		public CauldronItemRecipe read(Identifier id, PacketByteBuf buf)
		{
			Ingredient catalyst = Ingredient.fromPacket(buf);
			int waterUse = buf.readInt();
			int size = buf.readInt();
			List<Ingredient> inputs = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
			{
				inputs.add(Ingredient.fromPacket(buf));
			}
			ItemStack output = buf.readItemStack();
			return new CauldronItemRecipe(id, catalyst, waterUse, inputs, output);
		}

		@Override
		public void write(PacketByteBuf buf, CauldronItemRecipe recipe)
		{
			recipe.catalyst.write(buf);
			buf.writeInt(recipe.waterUse);
			buf.writeInt(recipe.inputs.size());
			for(int i = 0; i < recipe.inputs.size(); i++)
			{
				recipe.inputs.get(i).write(buf);
			}
			buf.writeItemStack(recipe.getOutput());
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, Ingredient catalyst, int waterUse, List<Ingredient> inputs, ItemStack output);
		}
	}
}
