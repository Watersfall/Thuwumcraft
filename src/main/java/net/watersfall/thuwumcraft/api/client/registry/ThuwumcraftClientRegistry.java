package net.watersfall.thuwumcraft.api.client.registry;

import net.watersfall.thuwumcraft.api.client.gui.BookRecipeType;
import net.watersfall.thuwumcraft.api.client.gui.RecipeTabType;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRegistryImpl;

public interface ThuwumcraftClientRegistry
{
	public static final ThuwumcraftRegistry<BookRecipeType, RecipeTabType> RECIPE_TAB_TYPE = new ThuwumcraftRegistryImpl<>();
}
