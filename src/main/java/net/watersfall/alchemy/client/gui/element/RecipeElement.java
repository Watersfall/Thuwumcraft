package net.watersfall.alchemy.client.gui.element;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class RecipeElement implements Element, Drawable, TooltipElement
{
	public ItemElement[] items;

	public RecipeElement(ItemElement[] items)
	{
		this.items = items;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		for(int i = 0; i < items.length; i++)
		{
			items[i].render(matrices, mouseX, mouseY, delta);
		}
	}

	@Override
	public ItemStack getTooltip(int mouseX, int mouseY)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i].isMouseOver(mouseX, mouseY))
			{
				return items[i].getTooltip(mouseX, mouseY);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i].isMouseOver(mouseX, mouseY))
			{
				return true;
			}
		}
		return false;
	}
}
