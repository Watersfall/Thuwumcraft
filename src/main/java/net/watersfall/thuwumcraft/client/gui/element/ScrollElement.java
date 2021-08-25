package net.watersfall.thuwumcraft.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ScrollElement implements Element, Drawable, Selectable
{
	private static final Identifier BACKGROUND_TEXTURE = Thuwumcraft.getId("textures/gui/container/thaumatorium.png");

	public int x, minY, maxY, size;
	public double y, step;

	public ScrollElement(int x, int minY, int maxY, int size)
	{
		this.x = x;
		this.y = 0;
		this.minY = minY;
		this.maxY = maxY;
		this.size = size - (maxY - minY) - 5;
		this.step = (float)size / (float)(maxY - minY);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		DrawableHelper.drawTexture(matrices, x, Y(), 176, 36, 4, 7, 256, 256);
	}

	public int y()
	{
		return minY - (int)y - size - 11;
	}

	private int Y()
	{
		return (int)((y / size) * (maxY - minY)) + minY;
	}

	@Override
	public SelectionType getType()
	{
		return SelectionType.NONE;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) { }

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount)
	{
		if(size > 0)
		{
			y += -(int)amount * step;
			if(y < 0)
			{
				y = 0;
			}
			if(y >= size)
			{
				y = size;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
	{
		if(size > 0)
		{
			y += deltaY / step;
			if(y < 0)
			{
				y = 0;
			}
			if(y >= size)
			{
				y = size;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX > this.x && mouseX < this.x + 4 && mouseY > Y() && mouseY < Y() + 7;
	}
}
