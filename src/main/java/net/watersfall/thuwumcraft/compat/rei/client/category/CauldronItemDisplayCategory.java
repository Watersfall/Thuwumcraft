package net.watersfall.thuwumcraft.compat.rei.client.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.CauldronItemCraftingDisplay;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.ArrayList;
import java.util.List;

public class CauldronItemDisplayCategory implements DisplayCategory<CauldronItemCraftingDisplay>
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.cauldron");

	@Override
	public Renderer getIcon()
	{
		return EntryStacks.of(ThuwumcraftItems.BREWING_CAULDRON_BLOCK);
	}

	@Override
	public Text getTitle()
	{
		return name;
	}

	@Override
	public CategoryIdentifier<? extends CauldronItemCraftingDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_ITEM;
	}

	@Override
	public List<Widget> setupDisplay(CauldronItemCraftingDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		widgets.addAll(DisplayCategory.super.setupDisplay(display, bounds));
		Point origin = new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 9);
		int start = origin.x + 9 - (display.getInputEntries().size() * 9);
		for(int i = 0; i < display.getInputEntries().size(); i++)
		{
			int x = start + i * 18;
			widgets.add(Widgets.createSlot(new Point(x, origin.y + 18)).entries(display.getInputEntries().get(i)).disableBackground());
		}
		widgets.add(Widgets.createArrow(new Point(origin.x, bounds.y + 18)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 18, bounds.y + 18)).entries(EntryIngredients.ofIngredient(display.catalyst)).disableBackground());
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + bounds.width - 36, bounds.y + 18)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width - 36, bounds.y + 18)).disableBackground().entries(display.getOutputEntries().get(0)));
		return widgets;
	}
}
