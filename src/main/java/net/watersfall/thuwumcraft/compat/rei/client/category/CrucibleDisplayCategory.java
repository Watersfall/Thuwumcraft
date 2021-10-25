package net.watersfall.thuwumcraft.compat.rei.client.category;

import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.TransferDisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.CrucibleDisplay;

import java.util.ArrayList;
import java.util.List;

public class CrucibleDisplayCategory implements TransferDisplayCategory<CrucibleDisplay>
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.crucible");

	@Override
	public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, CrucibleDisplay display, IntList redSlots)
	{

	}

	@Override
	public Renderer getIcon()
	{
		return EntryStacks.of(Items.CAULDRON);
	}

	@Override
	public Text getTitle()
	{
		return name;
	}

	@Override
	public CategoryIdentifier<? extends CrucibleDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CRUCIBLE;
	}

	@Override
	public List<Widget> setupDisplay(CrucibleDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>();
		widgets.addAll(TransferDisplayCategory.super.setupDisplay(display, bounds));
		Point origin = new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 9);
		int start = origin.x + 9 - (display.aspects.size() * 9);
		for(int i = 0; i < display.aspects.size(); i++)
		{
			int x = start + i * 18;
			ItemStack stack = display.aspects.get(i).toItemStack();
			widgets.add(Widgets.createSlot(new Point(x, origin.y + 18)).entry(EntryStacks.of(stack)).disableBackground());
		}
		widgets.add(Widgets.createArrow(new Point(origin.x, bounds.y + 18)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 18, bounds.y + 18)).entries(display.getInputEntries().get(0)).disableBackground());
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + bounds.width - 36, bounds.y + 18)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width - 36, bounds.y + 18)).disableBackground().entries(display.getOutputEntries().get(0)));
		return widgets;
	}

	@Override
	public int getDisplayHeight()
	{
		return 66;
	}
}
