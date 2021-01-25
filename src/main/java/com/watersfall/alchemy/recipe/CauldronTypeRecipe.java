package com.watersfall.alchemy.recipe;

import com.google.gson.JsonObject;
import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CauldronTypeRecipe implements Recipe<BrewingCauldronInventory>
{
	public final Identifier id;
	public final Ingredient input;
	public final int waterUse;
	public final int uses;

	public CauldronTypeRecipe(Identifier id, Ingredient input, int waterUse)
	{
		this(id, input, waterUse, 0);
	}

	public CauldronTypeRecipe(Identifier id, Ingredient input, int waterUse, int uses)
	{
		this.id = id;
		this.input = input;
		this.waterUse = waterUse;
		this.uses = uses;
	}

	@Override
	public boolean matches(BrewingCauldronInventory inv, World world)
	{
		return input.test(inv.getInput().get(0));
	}

	@Override
	public ItemStack craft(BrewingCauldronInventory inv)
	{
		return null;
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return null;
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyMod.CAULDRON_TYPE_RECIPE_SERIALIZER;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyMod.CAULDRON_TYPE_RECIPE_TYPE;
	}

	public static class Serializer implements RecipeSerializer<CauldronTypeRecipe>
	{
		private final CauldronTypeRecipe.Serializer.RecipeFactory<CauldronTypeRecipe> recipeFactory;
		public Serializer(CauldronTypeRecipe.Serializer.RecipeFactory<CauldronTypeRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public CauldronTypeRecipe read(Identifier id, JsonObject json)
		{
			Ingredient input = Ingredient.fromJson(json.get("items").getAsJsonArray());
			int waterUse = json.get("water_use").getAsInt();
			return new CauldronTypeRecipe(id, input, waterUse);
		}

		@Override
		public CauldronTypeRecipe read(Identifier id, PacketByteBuf buf)
		{
			int waterUse = buf.readInt();
			Ingredient input = Ingredient.fromPacket(buf);
			return new CauldronTypeRecipe(id, input, waterUse);
		}

		@Override
		public void write(PacketByteBuf buf, CauldronTypeRecipe recipe)
		{
			buf.writeInt(recipe.waterUse);
			recipe.input.write(buf);
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, Ingredient input, int waterUse);
		}
	}
}
