package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Ingredient;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.CauldronItemRecipe;

import java.util.List;
import java.util.Optional;

public class CauldronItemCraftingDisplay extends BasicDisplay
{
	public final Ingredient catalyst;

	public CauldronItemCraftingDisplay(CauldronItemRecipe recipe)
	{
		super(
				EntryIngredients.ofIngredients(recipe.getInputs()),
				List.of(EntryIngredients.of(recipe.getOutput())),
				Optional.of(recipe.getId())
		);
		catalyst = recipe.getCatalyst();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_ITEM;
	}
}
