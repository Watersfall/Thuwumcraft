package com.watersfall.alchemy.recipe;

import com.google.gson.JsonObject;
import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Locale;

public class CauldronTypeRecipe implements Recipe<BrewingCauldronInventory>
{
	public final Identifier id;
	public final Ingredient input;
	public final int waterUse;
	public final int uses;
	public final CraftingAction craftingAction;
	public final ItemStack output;

	public CauldronTypeRecipe(Identifier id, Ingredient input, int waterUse, CraftingAction action, int uses, ItemStack output)
	{
		this.id = id;
		this.input = input;
		this.waterUse = waterUse;
		this.craftingAction = action;
		this.uses = uses;
		this.output = output;
	}

	@Override
	public boolean matches(BrewingCauldronInventory inv, World world)
	{
		return input.test(inv.getInput().get(0));
	}

	@Override
	public ItemStack craft(BrewingCauldronInventory inv)
	{
		return this.output;
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
			CraftingAction action = CraftingAction.valueOf(json.get("action").getAsString().toUpperCase(Locale.ROOT));
			int waterUse = json.get("water_use").getAsInt();
			int uses = 0;
			ItemStack output = ItemStack.EMPTY;
			if(json.get("uses") != null)
			{
				uses = json.get("uses").getAsInt();
			}
			if(json.get("output") != null)
			{
				output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get("output").getAsString())));
			}
			return new CauldronTypeRecipe(id, input, waterUse, action, uses, output);
		}

		@Override
		public CauldronTypeRecipe read(Identifier id, PacketByteBuf buf)
		{
			int waterUse = buf.readInt();
			CraftingAction action = buf.readEnumConstant(CraftingAction.class);
			int uses = buf.readInt();
			Ingredient input = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			return new CauldronTypeRecipe(id, input, waterUse, action, uses, output);
		}

		@Override
		public void write(PacketByteBuf buf, CauldronTypeRecipe recipe)
		{
			buf.writeInt(recipe.waterUse);
			buf.writeEnumConstant(recipe.craftingAction);
			buf.writeInt(recipe.uses);
			buf.writeItemStack(recipe.getOutput());
			recipe.input.write(buf);
			buf.writeItemStack(recipe.output);
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, Ingredient input, int waterUse, CraftingAction action, int uses, ItemStack output);
		}
	}

	public enum CraftingAction
	{
		CREATE_LADLE,
		CREATE_WEAPON,
		CREATE_ITEM
	}
}
