package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.CauldronIngredient;

import java.util.List;
import java.util.Optional;

public class CauldronEffectDisplay extends BasicDisplay
{
	public final List<StatusEffectInstance> effects;
	public final int color;

	public CauldronEffectDisplay(CauldronIngredient recipe)
	{
		super(
				List.of(EntryIngredients.of(recipe.getInput())),
				List.of(EntryIngredients.of(recipe.getOutput())),
				Optional.of(recipe.getId())
		);
		this.effects = recipe.getEffects();
		this.color = recipe.getColor();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_EFFECT;
	}
}
