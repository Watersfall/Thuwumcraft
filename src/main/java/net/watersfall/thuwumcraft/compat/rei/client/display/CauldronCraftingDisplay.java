package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.CauldronIngredientRecipe;

import java.util.List;
import java.util.Optional;

public class CauldronCraftingDisplay extends BasicDisplay
{
	public CauldronCraftingDisplay(CauldronIngredientRecipe recipe)
	{
		super(
				List.of(EntryIngredients.ofIngredient(recipe.getInput())),
				List.of(EntryIngredients.of(recipe.getOutput())),
				Optional.of(recipe.getId())
		);
		if(recipe.getOutput().isEmpty())
		{
			outputs = List.of(EntryIngredients.ofIngredient(recipe.getInput()));
		}
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_CRAFTING;
	}
}
