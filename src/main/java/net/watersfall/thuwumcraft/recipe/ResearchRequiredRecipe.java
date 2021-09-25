package net.watersfall.thuwumcraft.recipe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.List;
import java.util.Optional;

/**
 * For my recipes
 * @param <T> inventory type
 */
public abstract class ResearchRequiredRecipe<T extends Inventory> implements Recipe<T>
{
	protected final Identifier id;
	protected final List<Identifier> research;

	public ResearchRequiredRecipe(Identifier id, List<Identifier> research)
	{
		this.id = id;
		this.research = research;
	}

	public boolean playerHasResearch(PlayerEntity player)
	{
		if(this.research.size() > 0)
		{
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			if(optional.isPresent())
			{
				PlayerResearchAbility ability = optional.get();
				for(int i = 0; i < this.research.size(); i++)
				{
					if(!ability.hasResearch(this.research.get(i)))
					{
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return true;
	}
}
