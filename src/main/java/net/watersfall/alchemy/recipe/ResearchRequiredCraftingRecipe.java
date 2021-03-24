package net.watersfall.alchemy.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

public interface ResearchRequiredCraftingRecipe
{
	boolean matches(CraftingInventory craftingInventory, World world, PlayerResearchAbility ability);
}
