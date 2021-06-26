package net.watersfall.thuwumcraft.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;

import java.util.List;

/**
 * For wrapping vanilla recipes
 */
public interface ResearchRequiredCraftingRecipe
{
	List<Identifier> getResearch();

	CraftingRecipe getRecipe();

	default boolean matches(CraftingInventory craftingInventory, World world, PlayerResearchAbility ability)
	{
		if(!getRecipe().matches(craftingInventory, world))
		{
			return false;
		}
		else
		{
			for(int i = 0; i < getResearch().size(); i++)
			{
				if(!ability.hasResearch(getResearch().get(i)))
				{
					return false;
				}
			}
		}
		return true;
	}
}
