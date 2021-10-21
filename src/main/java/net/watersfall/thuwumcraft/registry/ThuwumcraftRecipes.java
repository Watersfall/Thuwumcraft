package net.watersfall.thuwumcraft.registry;

import net.minecraft.recipe.*;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.recipe.*;

public class ThuwumcraftRecipes
{
	public static final RecipeType<CauldronIngredient> CAULDRON_INGREDIENTS;
	public static final RecipeType<CauldronIngredientRecipe> CAULDRON_INGREDIENT;
	public static final RecipeType<CauldronItemRecipe> CAULDRON_ITEM;
	public static final RecipeType<PedestalRecipe> PEDESTAL;
	public static final RecipeType<GrindingRecipe> GRINDING;
	public static final RecipeType<AspectIngredient> ASPECT_INGREDIENTS;
	public static final RecipeType<CrucibleRecipe> CRUCIBLE;
	public static final RecipeType<NekomancyRecipe> NEKOMANCY;
	public static final RecipeType<WandRecipe> WAND;
	public static final RecipeSerializer<CauldronIngredient> CAULDRON_INGREDIENTS_SERIALIZER;
	public static final RecipeSerializer<CauldronIngredientRecipe> CAULDRON_INGREDIENT_SERIALIZER;
	public static final RecipeSerializer<CauldronItemRecipe> CAULDRON_ITEM_SERIALIZER;
	public static final RecipeSerializer<PedestalRecipe> PEDESTAL_SERIALIZER;
	public static final RecipeSerializer<GrindingRecipe> GRINDING_SERIALIZER;
	public static final RecipeSerializer<AspectIngredient> ASPECT_INGREDIENT_SERIALIZER;
	public static final RecipeSerializer<CrucibleRecipe> CRUCIBLE_SERIALIZER;
	public static final RecipeSerializer<ResearchUnlockedRecipe<ShapedRecipe>> RESEARCH_UNLOCKED_SHAPED_SERIALIZER;
	public static final RecipeSerializer<ResearchUnlockedRecipe<ShapelessRecipe>> RESEARCH_UNLOCKED_SHAPELESS_SERIALIZER;
	public static final RecipeSerializer<NekomancyRecipe> NEKOMANCY_SERIALIZER;
	public static final RecipeSerializer<AspectCraftingShapedRecipe> ASPECT_SHAPED_SERIALIZER;
	public static final RecipeSerializer<BindingRecipe> BINDING_SERIALIZER;
	public static final RecipeSerializer<WandRecipe> WAND_SERIALIZER;
	public static final RecipeSerializer<AbilityRecipe<ShapelessRecipe>> SHAPELESS_ABILITY;
	public static final RecipeSerializer<GogglesRecipe> GOGGLES_SERIALIZER;
	
	static 
	{
		CAULDRON_INGREDIENTS = getRecipeType("cauldron_ingredient");
		CAULDRON_INGREDIENT = getRecipeType("cauldron_recipe");
		CAULDRON_ITEM = getRecipeType("cauldron_item");
		PEDESTAL = getRecipeType("pedestal_crafting");
		GRINDING = getRecipeType("grinding");
		ASPECT_INGREDIENTS = getRecipeType("aspect_ingredient");
		CRUCIBLE = getRecipeType("crucible_recipe");
		NEKOMANCY = getRecipeType("nekomancy_recipe");
		WAND = getRecipeType("wand_recipe");
		CAULDRON_INGREDIENTS_SERIALIZER = new CauldronIngredient.Serializer();
		CAULDRON_INGREDIENT_SERIALIZER = new CauldronIngredientRecipe.Serializer();
		CAULDRON_ITEM_SERIALIZER = new CauldronItemRecipe.Serializer();
		PEDESTAL_SERIALIZER = new PedestalRecipe.Serializer();
		GRINDING_SERIALIZER = new GrindingRecipe.Serializer();
		ASPECT_INGREDIENT_SERIALIZER = new AspectIngredient.Serializer();
		CRUCIBLE_SERIALIZER = new CrucibleRecipe.Serializer();
		RESEARCH_UNLOCKED_SHAPED_SERIALIZER = new ResearchUnlockedRecipe.Serializer<>(RecipeSerializer.SHAPED);
		RESEARCH_UNLOCKED_SHAPELESS_SERIALIZER = new ResearchUnlockedRecipe.Serializer<>(RecipeSerializer.SHAPELESS);
		NEKOMANCY_SERIALIZER = new NekomancyRecipe.Serializer();
		ASPECT_SHAPED_SERIALIZER =new AspectCraftingShapedRecipe.Serializer();
		BINDING_SERIALIZER = new BindingRecipe.Serializer();
		WAND_SERIALIZER = new WandRecipe.Serializer();
		SHAPELESS_ABILITY = new AbilityRecipe.Serializer(RecipeSerializer.SHAPELESS);
		GOGGLES_SERIALIZER = new GogglesRecipe.Serializer();
	}

	public static void register()
	{
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("cauldron_ingredient"), ThuwumcraftRecipes.CAULDRON_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("cauldron_recipe"), ThuwumcraftRecipes.CAULDRON_INGREDIENT);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("cauldron_item"), ThuwumcraftRecipes.CAULDRON_ITEM);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("pedestal_crafting"), ThuwumcraftRecipes.PEDESTAL);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("grinding"), ThuwumcraftRecipes.GRINDING);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("aspect_ingredient"), ASPECT_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("crucible_recipe"), CRUCIBLE);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("nekomancy_recipe"), NEKOMANCY);
		Registry.register(Registry.RECIPE_TYPE, Thuwumcraft.getId("wand_recipe"), WAND);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("cauldron_ingredient"), ThuwumcraftRecipes.CAULDRON_INGREDIENTS_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("cauldron_recipe"), ThuwumcraftRecipes.CAULDRON_INGREDIENT_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("cauldron_item"), ThuwumcraftRecipes.CAULDRON_ITEM_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("pedestal_crafting"), ThuwumcraftRecipes.PEDESTAL_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("grinding"), ThuwumcraftRecipes.GRINDING_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("aspect_ingredient"), ASPECT_INGREDIENT_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("crucible_recipe"), CRUCIBLE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("research_unlocked_shaped_recipe"), RESEARCH_UNLOCKED_SHAPED_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("research_unlocked_shapeless_recipe"), RESEARCH_UNLOCKED_SHAPELESS_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("nekomancy_recipe"), NEKOMANCY_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("aspect_shaped"), ASPECT_SHAPED_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("binding"), BINDING_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("wand_recipe"), WAND_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("shapeless_ability"), SHAPELESS_ABILITY);
		Registry.register(Registry.RECIPE_SERIALIZER, Thuwumcraft.getId("goggles"), GOGGLES_SERIALIZER);
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
