package net.watersfall.thuwumcraft.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemElement implements Element, Drawable, TooltipElement, Selectable
{
	protected final ItemStack[] stacks;
	protected final int x;
	protected final int y;
	protected final int offsetX;
	protected final int offsetY;
	protected final Element parent;
	protected final List<List<Text>> tooltips;

	public ItemElement(ItemStack[] stacks, int x, int y)
	{
		this(stacks, x, y, x, y, null);
	}

	public ItemElement(ItemStack[] stacks, int x, int y, int offsetX, int offsetY)
	{
		this(stacks, x, y, offsetX, offsetY, null);
	}

	public ItemElement(ItemStack[] stacks, int x, int y, int offsetX, int offsetY, Element parent)
	{
		this.stacks = stacks;
		this.x = x;
		this.y = y;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.parent = parent;
		this.tooltips = new ArrayList<>(stacks.length);
		for(int i = 0; i < stacks.length; i++)
		{
			tooltips.add(null);
		}
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
				MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, stacks[index], offsetX, offsetY);
			}
			else
			{
				MinecraftClient.getInstance().getItemRenderer().renderInGui(stacks[index], x, y);
				MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, stacks[index], x, y);
			}
		}
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		if(stacks.length > 0)
		{
			int index = (int) (MinecraftClient.getInstance().world.getTime() / (20F) % stacks.length);
			if(this.tooltips.get(index) == null)
			{
				this.tooltips.set(index, stacks[index].getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL));
			}
			return this.tooltips.get(index);
		}
		return new ArrayList<>();
	}

	@Override
	public SelectionType getType()
	{
		return SelectionType.NONE;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder)
	{

	}
}
