package net.watersfall.alchemy.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyRecipes
{
	public static final RecipeType<CauldronIngredient> CAULDRON_INGREDIENTS;
	public static final RecipeType<CauldronIngredientRecipe> CAULDRON_INGREDIENT_RECIPE;
	public static final RecipeType<CauldronItemRecipe> CAULDRON_ITEM_RECIPE;
	public static final RecipeType<PedestalRecipe> PEDESTAL_RECIPE;
	public static final RecipeType<GrindingRecipe> GRINDING_RECIPE;
	public static final RecipeType<AspectIngredient> ASPECT_INGREDIENTS;
	public static final RecipeType<CrucibleRecipe> CRUCIBLE_RECIPE;
	public static final RecipeType<ResearchUnlockedShapedRecipe> RESEARCH_UNLOCKED_SHAPED_RECIPE;
	public static final RecipeType<ResearchUnlockedShapelessRecipe> RESEARCH_UNLOCKED_SHAPELESS_RECIPE;
	public static final RecipeType<NekomancyRecipe> NEKOMANCY_RECIPE;
	public static final RecipeSerializer<CauldronIngredient> CAULDRON_INGREDIENTS_SERIALIZER;
	public static final RecipeSerializer<CauldronIngredientRecipe> CAULDRON_INGREDIENT_RECIPE_SERIALIZER;
	public static final RecipeSerializer<CauldronItemRecipe> CAULDRON_ITEM_RECIPE_SERIALIZER;
	public static final RecipeSerializer<PedestalRecipe> PEDESTAL_RECIPE_SERIALIZER;
	public static final RecipeSerializer<GrindingRecipe> GRINDING_RECIPE_SERIALIZER;
	public static final RecipeSerializer<AspectIngredient> ASPECT_INGREDIENT_SERIALIZER;
	public static final RecipeSerializer<CrucibleRecipe> CRUCIBLE_RECIPE_SERIALIZER;
	public static final RecipeSerializer<ResearchUnlockedShapedRecipe> RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER;
	public static final RecipeSerializer<ResearchUnlockedShapelessRecipe> RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER;
	public static final RecipeSerializer<NekomancyRecipe> NEKOMANCY_RECIPE_SERIALIZER;
	
	static 
	{
		CAULDRON_INGREDIENTS = getRecipeType("cauldron_ingredient");
		CAULDRON_INGREDIENT_RECIPE = getRecipeType("cauldron_recipe");
		CAULDRON_ITEM_RECIPE = getRecipeType("cauldron_item");
		PEDESTAL_RECIPE = getRecipeType("pedestal_crafting");
		GRINDING_RECIPE = getRecipeType("grinding");
		ASPECT_INGREDIENTS = getRecipeType("aspect_ingredient");
		CRUCIBLE_RECIPE = getRecipeType("crucible_recipe");
		RESEARCH_UNLOCKED_SHAPED_RECIPE = getRecipeType("research_unlocked_shaped_recipe");
		RESEARCH_UNLOCKED_SHAPELESS_RECIPE = getRecipeType("research_unlocked_shapeless_recipe");
		NEKOMANCY_RECIPE = getRecipeType("nekomancy_recipe");
		CAULDRON_INGREDIENTS_SERIALIZER = new CauldronIngredient.Serializer(CauldronIngredient::new);
		CAULDRON_INGREDIENT_RECIPE_SERIALIZER = new CauldronIngredientRecipe.Serializer(CauldronIngredientRecipe::new);
		CAULDRON_ITEM_RECIPE_SERIALIZER = new CauldronItemRecipe.Serializer(CauldronItemRecipe::new);
		PEDESTAL_RECIPE_SERIALIZER = new PedestalRecipe.Serializer(PedestalRecipe::new);
		GRINDING_RECIPE_SERIALIZER = new GrindingRecipe.Serializer(GrindingRecipe::new);
		ASPECT_INGREDIENT_SERIALIZER = new AspectIngredient.Serializer(AspectIngredient::new);
		CRUCIBLE_RECIPE_SERIALIZER = new CrucibleRecipe.Serializer(CrucibleRecipe::new);
		RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER = new ResearchUnlockedShapedRecipe.Serializer();
		RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER = new ResearchUnlockedShapelessRecipe.Serializer();
		NEKOMANCY_RECIPE_SERIALIZER = new NekomancyRecipe.Serializer();
	}

	public static void register()
	{
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("cauldron_ingredient"), AlchemyRecipes.CAULDRON_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("cauldron_recipe"), AlchemyRecipes.CAULDRON_INGREDIENT_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("cauldron_item"), AlchemyRecipes.CAULDRON_ITEM_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("pedestal_crafting"), AlchemyRecipes.PEDESTAL_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("grinding"), AlchemyRecipes.GRINDING_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("aspect_ingredient"), ASPECT_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("crucible_recipe"), CRUCIBLE_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("research_unlocked_shaped_recipe"), RESEARCH_UNLOCKED_SHAPED_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("research_unlocked_shapeless_recipe"), RESEARCH_UNLOCKED_SHAPELESS_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, AlchemyMod.getId("nekomancy_recipe"), NEKOMANCY_RECIPE);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("cauldron_ingredient"), AlchemyRecipes.CAULDRON_INGREDIENTS_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("cauldron_recipe"), AlchemyRecipes.CAULDRON_INGREDIENT_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("cauldron_item"), AlchemyRecipes.CAULDRON_ITEM_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("pedestal_crafting"), AlchemyRecipes.PEDESTAL_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("grinding"), AlchemyRecipes.GRINDING_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("aspect_ingredient"), ASPECT_INGREDIENT_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("crucible_recipe"), CRUCIBLE_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("research_unlocked_shaped_recipe"), RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("research_unlocked_shapeless_recipe"), RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, AlchemyMod.getId("nekomancy_recipe"), NEKOMANCY_RECIPE_SERIALIZER);
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
