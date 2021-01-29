package com.watersfall.alchemy.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public class AlchemyModRecipes
{
	public static final RecipeType<CauldronIngredient> CAULDRON_INGREDIENTS;
	public static final RecipeType<CauldronIngredientRecipe> CAULDRON_INGREDIENT_RECIPE;
	public static final RecipeType<CauldronItemRecipe> CAULDRON_ITEM_RECIPE;
	public static final RecipeType<PedestalRecipe> PEDESTAL_RECIPE;
	public static final RecipeSerializer<CauldronIngredient> CAULDRON_INGREDIENTS_SERIALIZER;
	public static final RecipeSerializer<CauldronIngredientRecipe> CAULDRON_INGREDIENT_RECIPE_SERIALIZER;
	public static final RecipeSerializer<CauldronItemRecipe> CAULDRON_ITEM_RECIPE_SERIALIZER;
	public static final RecipeSerializer<PedestalRecipe> PEDESTAL_RECIPE_SERIALIZER;
	
	static 
	{
		CAULDRON_INGREDIENTS = getRecipeType("cauldron_ingredient");
		CAULDRON_INGREDIENT_RECIPE = getRecipeType("cauldron_recipe");
		CAULDRON_ITEM_RECIPE = getRecipeType("cauldron_item");
		PEDESTAL_RECIPE = getRecipeType("pedestal_crafting");
		CAULDRON_INGREDIENTS_SERIALIZER = new CauldronIngredient.Serializer(CauldronIngredient::new);
		CAULDRON_INGREDIENT_RECIPE_SERIALIZER = new CauldronIngredientRecipe.Serializer(CauldronIngredientRecipe::new);
		CAULDRON_ITEM_RECIPE_SERIALIZER = new CauldronItemRecipe.Serializer(CauldronItemRecipe::new);
		PEDESTAL_RECIPE_SERIALIZER = new PedestalRecipe.Serializer(PedestalRecipe::new);

	}

	private static <T extends Recipe<?>> RecipeType<T> getRecipeType(String string)
	{
		return new RecipeType<T>()
		{
			@Override
			public String toString()
			{
				return string;
			}
		};
	}
}
