package net.watersfall.thuwumcraft.api.client.gui;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.watersfall.thuwumcraft.client.gui.element.RecipeElement;

import java.util.HashMap;

@FunctionalInterface
public interface RecipeTabType
{
	public static final Registry REGISTRY = new Registry();

	RecipeElement generateRecipeLayout(Recipe<?> recipe, int x, int y, int width, int height);

	public static class Registry
	{
		private final HashMap<RecipeType<?>, RecipeTabType> map = new HashMap<>();

		public void register(RecipeType<?> type, RecipeTabType layout)
		{
			this.map.put(type, layout);
		}

		public RecipeTabType get(RecipeType<?> type)
		{
			return this.map.get(type);
		}
	}
}
