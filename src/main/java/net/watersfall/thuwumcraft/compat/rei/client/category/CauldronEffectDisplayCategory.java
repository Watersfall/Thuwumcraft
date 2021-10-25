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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.compat.rei.client.ThuwumcraftReiCompatClient;
import net.watersfall.thuwumcraft.compat.rei.client.display.CauldronEffectDisplay;

import java.util.ArrayList;
import java.util.List;

public class CauldronEffectDisplayCategory implements TransferDisplayCategory<CauldronEffectDisplay>
{
	private static final Text name = new TranslatableText("thuwumcraft.rei.category.brewing");

	@Override
	public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, CauldronEffectDisplay display, IntList redSlots)
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
	public CategoryIdentifier<? extends CauldronEffectDisplay> getCategoryIdentifier()
	{
		return ThuwumcraftReiCompatClient.CAULDRON_EFFECT;
	}

	@Override
	public List<Widget> setupDisplay(CauldronEffectDisplay display, Rectangle bounds)
	{
		List<Widget> widgets = new ArrayList<>(TransferDisplayCategory.super.setupDisplay(display, bounds));
		Point point = new Point(bounds.x + 18, bounds.getCenterY() - 9);
		widgets.add(Widgets.createResultSlotBackground(point));
		widgets.add(Widgets.createSlot(point).entries(display.getInputEntries().get(0)).disableBackground());
		int y = bounds.getCenterY() - (display.effects.size() * 10) / 2;
		for(int i = 0; i < display.effects.size(); i++)
		{
			int index = i;
			widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
				matrices.push();
				MinecraftClient.getInstance().textRenderer.draw(
						matrices,
						new TranslatableText(display.effects.get(index).getTranslationKey()).setStyle(Style.EMPTY.withColor(display.color)),
						bounds.x + 46,
						y + index * 10,
						display.color
				);
				matrices.pop();
			})));
		}
		return widgets;
	}

	@Override
	public int getDisplayHeight()
	{
		return TransferDisplayCategory.super.getDisplayHeight();
	}
}
