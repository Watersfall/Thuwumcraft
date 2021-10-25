package net.watersfall.thuwumcraft.compat.rei.client.display;

import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.item.ItemStack;
import net.watersfall.thuwumcraft.recipe.GogglesRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GogglesDisplay extends DefaultCraftingDisplay<GogglesRecipe>
{
	public GogglesDisplay(GogglesRecipe recipe)
	{
		super(
				EntryIngredients.ofIngredients(recipe.getIngredients()),
				List.of(),
				Optional.of(recipe)
		);
		List<ItemStack> list = new ArrayList<>();
		for(ItemStack stack : recipe.getIngredients().get(0).getMatchingStacks())
		{
			stack = stack.copy();
			stack.getOrCreateNbt().putBoolean("thuwumcraft$goggles", true);
			list.add(stack);
		}
		outputs = List.of(EntryIngredients.ofItemStacks(list));
	}

	@Override
	public int getWidth()
	{
		return 2;
	}

	@Override
	public int getHeight()
	{
		return 1;
	}
}
