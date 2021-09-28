package net.watersfall.thuwumcraft.registry;

import net.minecraft.recipe.*;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.recipe.*;

public class ThuwumcraftRecipes
{
	public static final RecipeType<CauldronIngredient> CAULDRON_INGREDIENTS;
	public static final RecipeType<CauldronIngredientRecipe> CAULDRON_INGREDIENT_RECIPE;
	public static final RecipeType<CauldronItemRecipe> CAULDRON_ITEM_RECIPE;
	public static final RecipeType<PedestalRecipe> PEDESTAL_RECIPE;
	public static final RecipeType<GrindingRecipe> GRINDING_RECIPE;
	public static final RecipeType<AspectIngredient> ASPECT_INGREDIENTS;
	public static final RecipeType<CrucibleRecipe> CRUCIBLE_RECIPE;
	public static final RecipeType<NekomancyRecipe> NEKOMANCY_RECIPE;
	public static final RecipeType<WandRecipe> WAND;
	public static final RecipeSerializer<CauldronIngredient> CAULDRON_INGREDIENTS_SERIALIZER;
	public static final RecipeSerializer<CauldronIngredientRecipe> CAULDRON_INGREDIENT_RECIPE_SERIALIZER;
	public static final RecipeSerializer<CauldronItemRecipe> CAULDRON_ITEM_RECIPE_SERIALIZER;
	public static final RecipeSerializer<PedestalRecipe> PEDESTAL_RECIPE_SERIALIZER;
	public static final RecipeSerializer<GrindingRecipe> GRINDING_RECIPE_SERIALIZER;
	public static final RecipeSerializer<AspectIngredient> ASPECT_INGREDIENT_SERIALIZER;
	public static final RecipeSerializer<CrucibleRecipe> CRUCIBLE_RECIPE_SERIALIZER;
	public static final RecipeSerializer<ResearchUnlockedRecipe<ShapedRecipe>> RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER;
	public static final RecipeSerializer<ResearchUnlockedRecipe<ShapelessRecipe>> RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER;
	public static final RecipeSerializer<NekomancyRecipe> NEKOMANCY_RECIPE_SERIALIZER;
	public static final RecipeSerializer<AspectCraftingShapedRecipe> ASPECT_SHAPED_SERIALIZER;
	public static final RecipeSerializer<BindingRecipe> BINDING_RECIPE_SERIALIZER;
	public static final RecipeSerializer<WandRecipe> WAND_RECIPE_SERIALIZER;
	
	static 
	{
		CAULDRON_INGREDIENTS = getRecipeType("cauldron_ingredient");
		CAULDRON_INGREDIENT_RECIPE = getRecipeType("cauldron_recipe");
		CAULDRON_ITEM_RECIPE = getRecipeType("cauldron_item");
		PEDESTAL_RECIPE = getRecipeType("pedestal_crafting");
		GRINDING_RECIPE = getRecipeType("grinding");
		ASPECT_INGREDIENTS = getRecipeType("aspect_ingredient");
		CRUCIBLE_RECIPE = getRecipeType("crucible_recipe");
		NEKOMANCY_RECIPE = getRecipeType("nekomancy_recipe");
		WAND = getRecipeType("wand_recipe");
		CAULDRON_INGREDIENTS_SERIALIZER = new CauldronIngredient.Serializer();
		CAULDRON_INGREDIENT_RECIPE_SERIALIZER = new CauldronIngredientRecipe.Serializer();
		CAULDRON_ITEM_RECIPE_SERIALIZER = new CauldronItemRecipe.Serializer();
		PEDESTAL_RECIPE_SERIALIZER = new PedestalRecipe.Serializer();
		GRINDING_RECIPE_SERIALIZER = new GrindingRecipe.Serializer();
		ASPECT_INGREDIENT_SERIALIZER = new AspectIngredient.Serializer();
		CRUCIBLE_RECIPE_SERIALIZER = new CrucibleRecipe.Serializer();
		RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER = new ResearchUnlockedRecipe.Serializer<>(RecipeSerializer.SHAPED);
		RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER = new ResearchUnlockedRecipe.Serializer<>(RecipeSerializer.SHAPELESS);
		NEKOMANCY_RECIPE_SERIALIZER = new NekomancyRecipe.Serializer();
		ASPECT_SHAPED_SERIALIZER =new AspectCraftingShapedRecipe.Serializer();
		BINDING_RECIPE_SERIALIZER = new BindingRecipe.Serializer();
		WAND_RECIPE_SERIALIZER = new WandRecipe.Serializer();
	}

	public static void register()
	{
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("cauldron_ingredient"), ThuwumcraftRecipes.CAULDRON_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("cauldron_recipe"), ThuwumcraftRecipes.CAULDRON_INGREDIENT_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("cauldron_item"), ThuwumcraftRecipes.CAULDRON_ITEM_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("pedestal_crafting"), ThuwumcraftRecipes.PEDESTAL_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("grinding"), ThuwumcraftRecipes.GRINDING_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("aspect_ingredient"), ASPECT_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("crucible_recipe"), CRUCIBLE_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("nekomancy_recipe"), NEKOMANCY_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("wand_recipe"), WAND);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("cauldron_ingredient"), ThuwumcraftRecipes.CAULDRON_INGREDIENTS_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("cauldron_recipe"), ThuwumcraftRecipes.CAULDRON_INGREDIENT_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("cauldron_item"), ThuwumcraftRecipes.CAULDRON_ITEM_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("pedestal_crafting"), ThuwumcraftRecipes.PEDESTAL_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("grinding"), ThuwumcraftRecipes.GRINDING_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("aspect_ingredient"), ASPECT_INGREDIENT_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("crucible_recipe"), CRUCIBLE_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("research_unlocked_shaped_recipe"), RESEARCH_UNLOCKED_SHAPED_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("research_unlocked_shapeless_recipe"), RESEARCH_UNLOCKED_SHAPELESS_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("nekomancy_recipe"), NEKOMANCY_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("aspect_shaped"), ASPECT_SHAPED_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("binding"), BINDING_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("wand_recipe"), WAND_RECIPE_SERIALIZER);
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
