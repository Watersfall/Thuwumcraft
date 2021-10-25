package net.watersfall.thuwumcraft.compat.rei.client.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.crafting.DefaultCraftingCategory;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultShapedDisplay;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.CrystalDisplay;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.ArrayList;
import java.util.List;

public class CrystalDisplayCategory extends DefaultCraftingCategory
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.crafting");

	@Override
	public Renderer getIcon()
	{
		return EntryStacks.of(ThuwumcraftItems.ASPECT_CRAFTING_TABLE_BLOCK);
	}

	@Override
	public Text getTitle()
	{
		return name;
	}

	@Override
	public CategoryIdentifier<? extends CrystalDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CRYSTAL;
	}

	@Override
	public List<Widget> setupDisplay(DefaultCraftingDisplay display, Rectangle bounds)
	{
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
		Point[] points = new Point[]{
				new Point(startPoint.x - 17, startPoint.y + 1),
				new Point(startPoint.x - 17, startPoint.y + 37),
				new Point(startPoint.x + 55, startPoint.y + 1),
				new Point(startPoint.x + 55, startPoint.y + 37),
				new Point(startPoint.x + 19, startPoint.y - 17),
				new Point(startPoint.x + 19, startPoint.y + 37 + 18)
		};
		List<Widget> widgets = Lists.newArrayList();
		List<Widget> crystals = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60 + 18, startPoint.y + 18)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95 + 18, startPoint.y + 19)));
		for(int i = 0; i < points.length; i++)
		{
			crystals.add(Widgets.createSlot(points[i]));
		}
		List<? extends List<? extends EntryStack<?>>> input = display.getInputEntries();
		List<Slot> slots = Lists.newArrayList();
		if(display instanceof CrystalDisplay crystalDisplay)
		{
			for(int y = 0; y < 3; y++)
			{
				for(int x = 0; x < 3; x++)
				{
					slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
				}
			}
			for(int i = 0; i < input.size() - crystalDisplay.crystals.size(); i++)
			{
				if(display instanceof DefaultShapedDisplay)
				{
					if(!input.get(i).isEmpty())
					{
						slots.get(DefaultCraftingDisplay.getSlotWithSize(display, i, 3)).entries(input.get(i));
					}

				}
				else if(!input.get(i).isEmpty())
				{
					slots.get(i).entries(input.get(i));
				}
			}
			int index = input.size() - crystalDisplay.crystals.size();
			for(int i = 0; i < crystalDisplay.crystals.size(); i++)
			{
				crystals.add(Widgets.createSlot(points[i]).entries(input.get(index++)));
			}
		}
		widgets.addAll(slots);
		widgets.addAll(crystals);
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95 + 18, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}

	@Override
	public int getDisplayHeight()
	{
		return super.getDisplayHeight() + 36;
	}

	@Override
	public int getDisplayWidth(DefaultCraftingDisplay display)
	{
		return super.getDisplayWidth(display) + 36;
	}
}
