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
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.AspectDisplay;

import java.util.ArrayList;
import java.util.List;

public class AspectCategory implements DisplayCategory<AspectDisplay>
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.aspects");

	@Override
	public Renderer getIcon()
	{
		return EntryStacks.of(Aspects.FIRE.getItem());
	}

	@Override
	public Text getTitle()
	{
		return name;
	}

	@Override
	public CategoryIdentifier<? extends AspectDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.ASPECT;
	}

	@Override
	public List<Widget> setupDisplay(AspectDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>(DisplayCategory.super.setupDisplay(display, bounds));
		Point origin = new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 9);
		int start = origin.x + 9 - (display.getOutputEntries().size() * 9);
		for(int i = 0; i < display.getOutputEntries().size(); i++)
		{
			int x = start + i * 18;
			widgets.add(Widgets.createSlot(new Point(x, origin.y + 18)).entries(display.getOutputEntries().get(i)).disableBackground());
		}
		widgets.add(Widgets.createResultSlotBackground(new Point(origin.x, origin.y - 9)));
		widgets.add(Widgets.createSlot(new Point(origin.x, origin.y - 9)).disableBackground().entries(display.getInputEntries().get(0)));
		return widgets;
	}
}
