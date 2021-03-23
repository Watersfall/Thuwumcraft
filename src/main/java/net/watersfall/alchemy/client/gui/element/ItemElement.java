package net.watersfall.alchemy.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemElement implements Element, Drawable, TooltipElement
{
	protected final ItemStack[] stacks;
	protected final int x;
	protected final int y;
	protected final int offsetX;
	protected final int offsetY;
	protected final Element parent;

	public ItemElement(ItemStack[] stacks, int x, int y)
	{
		this.stacks = stacks;
		this.x = x;
		this.y = y;
		this.offsetX = x;
		this.offsetY = y;
		this.parent = null;
	}

	public ItemElement(ItemStack[] stacks, int x, int y, int offsetX, int offsetY)
	{
		this.stacks = stacks;
		this.x = x;
		this.y = y;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.parent = null;
	}

	public ItemElement(ItemStack[] stacks, int x, int y, int offsetX, int offsetY, Element parent)
	{
		this.stacks = stacks;
		this.x = x;
		this.y = y;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.parent = parent;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		if(parent != null)
		{
			return parent.isMouseOver(mouseX, mouseY);
		}
		return mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		if(stacks.length > 0)
		{
			int index = (int) (MinecraftClient.getInstance().world.getTime() / (20F) % stacks.length);
			if(isMouseOver(mouseX, mouseY))
			{
				MinecraftClient.getInstance().getItemRenderer().renderInGui(stacks[index], offsetX, offsetY);
			}
			else
			{
				MinecraftClient.getInstance().getItemRenderer().renderInGui(stacks[index], x, y);
			}
		}
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		if(stacks.length > 0)
		{
			int index = (int) (MinecraftClient.getInstance().world.getTime() / (20F) % stacks.length);
			return stacks[index].getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL);
		}
		return new ArrayList<>();
	}
}
