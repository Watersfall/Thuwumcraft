package net.watersfall.alchemy.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.client.gui.ResearchTab;

public class TabElement implements Element, Drawable, TooltipElement
{
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");

	private final ResearchTab tab;
	private final ItemElement items;
	private final int x;
	private final int y;
	private final boolean inverted;

	public TabElement(ResearchTab tab, int x, int y, boolean inverted)
	{
		this.x = x;
		this.y = y;
		this.inverted = inverted;
		this.tab = tab;
		ItemStack[] items = new ItemStack[tab.recipes.length];
		for(int i = 0; i < items.length; i++)
		{
			items[i] = tab.recipes[i].getOutput();
		}
		this.items = new ItemElement(items, x, y, inverted ? x + 4 : x - 4, y);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		MinecraftClient.getInstance().getTextureManager().bindTexture(ICONS);
		if(isMouseOver(mouseX, mouseY))
		{
			DrawableHelper.drawTexture(matrices, this.x, this.y, 24, 16, 24, 16, 256, 256);
			this.items.render(matrices, mouseX, mouseY, delta);
		}
		else
		{
			DrawableHelper.drawTexture(matrices, this.x, this.y, 28, 16, 20, 16, 256, 256);
			this.items.render(matrices, mouseX, mouseY, delta);
		}
	}

	@Override
	public ItemStack getTooltip(int mouseX, int mouseY)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX > x && mouseX < x + 24 && mouseY > y && mouseY < y + 16;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(button == 0 && isMouseOver(mouseX, mouseY))
		{
			MinecraftClient.getInstance().openScreen(this.tab);
			return true;
		}
		return false;
	}
}
