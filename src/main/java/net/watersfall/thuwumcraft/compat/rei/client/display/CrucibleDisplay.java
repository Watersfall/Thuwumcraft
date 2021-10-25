package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.CrucibleRecipe;

import java.util.List;
import java.util.Optional;

public class CrucibleDisplay extends BasicDisplay
{
	public final List<AspectStack> aspects;

	public CrucibleDisplay(CrucibleRecipe recipe)
	{
		super(
				List.of(EntryIngredients.ofIngredient(recipe.catalyst)),
				List.of(EntryIngredients.of(recipe.getOutput())),
				Optional.of(recipe.getId())
		);
		aspects = recipe.aspects;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CRUCIBLE;
	}
}
