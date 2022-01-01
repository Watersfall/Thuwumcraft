package net.watersfall.thuwumcraft.compat.rei.client.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.CauldronCraftingDisplay;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.ArrayList;
import java.util.List;

public class CauldronCraftingDisplayCategory implements DisplayCategory<CauldronCraftingDisplay>
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
	public CategoryIdentifier<? extends CauldronCraftingDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_CRAFTING;
	}

	@Override
	public List<Widget> setupDisplay(CauldronCraftingDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>(DisplayCategory.super.setupDisplay(display, bounds));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 18, bounds.getCenterY() - 9)).entries(display.getInputEntries().get(0)));
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + bounds.width - 36, bounds.getCenterY() - 9)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width - 36, bounds.getCenterY() - 9)).entries(display.getOutputEntries().get(0)).disableBackground());
		widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 9)));
		return widgets;
	}
}
