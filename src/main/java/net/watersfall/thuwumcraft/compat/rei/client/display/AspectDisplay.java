package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Ingredient;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.AspectIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AspectDisplay extends BasicDisplay
{
	public AspectDisplay(AspectIngredient recipe)
	{
		super(
				List.of(EntryIngredients.of(recipe.getInput())),
				List.of(),
				Optional.of(recipe.getId())
		);
		List<Ingredient> aspects = new ArrayList<>();
		for(AspectStack stack : recipe.getAspects())
		{
			aspects.add(Ingredient.ofStacks(stack.toItemStack()));
		}
		outputs = EntryIngredients.ofIngredients(aspects);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.ASPECT;
	}
}
