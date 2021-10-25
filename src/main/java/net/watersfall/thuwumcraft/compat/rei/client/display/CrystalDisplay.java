package net.watersfall.thuwumcraft.compat.rei.client.display;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultShapedDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.recipe.AspectCraftingShapedRecipe;
import net.watersfall.thuwumcraft.recipe.ResearchUnlockedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CrystalDisplay extends DefaultShapedDisplay
{
	public final List<EntryIngredient> crystals;
	private final List<EntryIngredient> inputs;

	public CrystalDisplay(AspectCraftingShapedRecipe recipe)
	{
		super(((ResearchUnlockedRecipe<ShapedRecipe>)recipe.getRecipe()).recipe);
		Item[] items = new Item[]{
				Aspects.ASPECT_TO_CRYSTAL.get(Aspects.WATER),
				Aspects.ASPECT_TO_CRYSTAL.get(Aspects.EARTH),
				Aspects.ASPECT_TO_CRYSTAL.get(Aspects.AIR),
				Aspects.ASPECT_TO_CRYSTAL.get(Aspects.FIRE),
				Aspects.ASPECT_TO_CRYSTAL.get(Aspects.ORDER),
				Aspects.ASPECT_TO_CRYSTAL.get(Aspects.DISORDER)
		};
		List<ItemStack> temp = new ArrayList<>();
		for(int i = 0; i < items.length; i++)
		{
			temp.add(ItemStack.EMPTY);
			for(int o = 0; o < recipe.getAspects().size(); o++)
			{
				if(recipe.getAspects().get(o).isOf(items[i]))
				{
					temp.set(i, recipe.getAspects().get(o).copy());
					break;
				}
			}
		}
		crystals = ImmutableList.copyOf(temp.stream().map(input -> EntryIngredient.of(EntryStacks.of(input))).toList());
		List<EntryIngredient> list = new ArrayList<>(super.getInputEntries());
		list.addAll(crystals);
		inputs = list;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CRYSTAL;
	}

	public List<EntryIngredient> getCrystals()
	{
		return crystals;
	}

	@Override
	public List<EntryIngredient> getInputEntries()
	{
		return inputs;
	}
}
