package net.watersfall.thuwumcraft.compat.rei.client.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.InfusionDisplay;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.ArrayList;
import java.util.List;

public class InfusionDisplayCategory implements DisplayCategory<InfusionDisplay>
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.infusion");

	@Override
	public Renderer getIcon()
	{
		return EntryStacks.of(ThuwumcraftItems.PEDESTAL);
	}

	@Override
	public Text getTitle()
	{
		return name;
	}

	@Override
	public CategoryIdentifier<? extends InfusionDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.INFUSION;
	}

	@Override
	public int getDisplayHeight()
	{
		return 150;
	}

	@Override
	public List<Widget> setupDisplay(InfusionDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		Point origin = new Point(bounds.getCenterX() - 9, bounds.getCenterY());
		widgets.add(Widgets.createSlot(origin).entries(display.getInputEntries().get(0)).disableBackground());
		int total = display.getInputEntries().size();
		for(int i = 1; i < total; i++)
		{
			double angle = Math.PI * 2 / (total - 1) * (i - 1) - (MathHelper.PI / 2F);
			int circleX = origin.x + (int)(40 * Math.cos(angle));
			int circleY = origin.y + (int)(40 * Math.sin(angle));
			widgets.add(Widgets.createSlot(new Point(circleX, circleY)).entries(display.getInputEntries().get(i)).disableBackground());
		}
		widgets.add(Widgets.createResultSlotBackground(new Point(origin.x, origin.y - 64)));
		widgets.add(Widgets.createSlot(new Point(origin.x, origin.y - 64)).disableBackground().entries(display.getOutputEntries().get(display.getOutputEntries().size() - 1)));
		int start = origin.x + 9 - (display.aspects.size() * 9);
		for(int i = 0; i < display.aspects.size(); i++)
		{
			ItemStack stack = new ItemStack(display.aspects.get(i).getAspect().getItem(), display.aspects.get(i).getCount());
			widgets.add(Widgets.createSlot(new Point(start + i * 18, origin.y + 56)).entry(EntryStacks.of(stack)).disableBackground());
		}
		return widgets;
	}
}
