package net.watersfall.thuwumcraft.api.client.gui;

import net.minecraft.recipe.Recipe;
import net.watersfall.thuwumcraft.client.gui.element.RecipeElement;

@FunctionalInterface
public interface RecipeTabType
{
	RecipeElement generateRecipeLayout(Recipe<?> type, int x, int y, int width, int height);
}
