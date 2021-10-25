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
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.CauldronCraftingDisplay;

import java.util.ArrayList;
import java.util.List;

public class CauldronCraftingDisplayCategory implements TransferDisplayCategory<CauldronCraftingDisplay>
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.cauldron");

	@Override
	public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, CauldronCraftingDisplay display, IntList redSlots)
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
	public CategoryIdentifier<? extends CauldronCraftingDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_CRAFTING;
	}

	@Override
	public List<Widget> setupDisplay(CauldronCraftingDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>(TransferDisplayCategory.super.setupDisplay(display, bounds));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 18, bounds.getCenterY() - 9)).entries(display.getInputEntries().get(0)));
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + bounds.width - 36, bounds.getCenterY() - 9)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width - 36, bounds.getCenterY() - 9)).entries(display.getOutputEntries().get(0)).disableBackground());
		widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 9)));
		return widgets;
	}
}
