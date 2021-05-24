package net.watersfall.thuwumcraft.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class RecipeElement implements Element, Drawable, TooltipElement
{
	public ItemElement[] items;
	public boolean twoPage;

	public RecipeElement(ItemElement[] items, boolean twoPage)
	{
		this.items = items;
		this.twoPage = twoPage;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		MinecraftClient.getInstance().getItemRenderer().zOffset += 100;
		for(int i = 0; i < items.length; i++)
		{
			items[i].render(matrices, mouseX, mouseY, delta);
		}
		MinecraftClient.getInstance().getItemRenderer().zOffset -= 100;
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i].isMouseOver(mouseX, mouseY))
			{
				return items[i].getTooltip(mouseX, mouseY);
			}
		}
		return Collections.emptyList();
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
