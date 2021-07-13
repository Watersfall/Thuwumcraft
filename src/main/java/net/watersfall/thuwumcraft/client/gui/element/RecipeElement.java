package net.watersfall.thuwumcraft.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class RecipeElement implements Element, Drawable, TooltipElement
{
	public ItemElement[] items;
	public Background background;

	public RecipeElement(ItemElement[] items, Background background)
	{
		this.items = items;
		this.background = background;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		background.render(matrices, mouseX, mouseY, delta);
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

	public record Background(int x, int y, int width, int height, Identifier texture)
	{
		public static final Background EMPTY = new Background(0, 0, 0, 0, null);

		public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
		{
			if(this != EMPTY)
			{
				RenderSystem.setShaderTexture(0, texture());
				DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height, width, height);
			}
		}
	}
}
