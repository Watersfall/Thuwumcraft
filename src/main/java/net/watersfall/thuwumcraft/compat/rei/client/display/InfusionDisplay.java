package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.PedestalRecipe;

import java.util.List;
import java.util.Optional;

public class InfusionDisplay extends BasicDisplay
{
	public final List<AspectStack> aspects;

	public InfusionDisplay(PedestalRecipe recipe)
	{
		super(
				EntryIngredients.ofIngredients(recipe.getIngredients()),
				List.of(EntryIngredients.of(recipe.getOutput())),
				Optional.of(recipe.getId())
		);
		aspects = recipe.getAspects();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.INFUSION;
	}
}
