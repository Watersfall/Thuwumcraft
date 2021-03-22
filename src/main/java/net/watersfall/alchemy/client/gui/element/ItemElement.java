package net.watersfall.alchemy.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ItemElement implements Element, Drawable, TooltipElement
{
	private final ItemStack[] stacks;
	private final int x;
	private final int y;

	public ItemElement(ItemStack[] stacks, int x, int y)
	{
		this.stacks = stacks;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		if(stacks.length > 0)
		{
			int index = (int) (MinecraftClient.getInstance().world.getTime() / (20F) % stacks.length);
			MinecraftClient.getInstance().getItemRenderer().renderInGui(stacks[index], x, y);
		}
	}

	@Override
	public ItemStack getTooltip(int mouseX, int mouseY)
	{
		if(stacks.length > 0)
		{
			int index = (int) (MinecraftClient.getInstance().world.getTime() / (20F) % stacks.length);
			return stacks[index];
		}
		return ItemStack.EMPTY;
	}
}
