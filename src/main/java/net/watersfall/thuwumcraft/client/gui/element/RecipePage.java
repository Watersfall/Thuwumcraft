package net.watersfall.thuwumcraft.client.gui.element;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class RecipePage implements Element, Drawable, TooltipElement
{
	private final RecipeElement[] elements;

	public RecipePage(RecipeElement... elements)
	{
		this.elements = elements;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		for(int i = 0; i < elements.length; i++)
		{
			elements[i].render(matrices, mouseX, mouseY, delta);
		}
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		for(int i = 0; i < this.elements.length; i++)
		{
			if(elements[i].isMouseOver(mouseX, mouseY))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		List<Text> list = Collections.emptyList();
		for(int i = 0; i < elements.length; i++)
		{
			if(!(list = elements[i].getTooltip(mouseX, mouseY)).isEmpty())
			{
				return list;
			}
		}
		return list;
	}
}
