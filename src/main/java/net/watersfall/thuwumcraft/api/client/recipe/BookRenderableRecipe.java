package net.watersfall.thuwumcraft.api.client.recipe;

import net.watersfall.thuwumcraft.api.client.gui.BookRecipeType;
import net.watersfall.thuwumcraft.api.client.gui.BookRecipeTypes;

public interface BookRenderableRecipe
{
	default BookRecipeType getBookType()
	{
		return BookRecipeTypes.CRAFTING;
	}
}
