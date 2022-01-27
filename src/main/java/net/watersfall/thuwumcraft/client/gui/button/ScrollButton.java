package net.watersfall.thuwumcraft.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.client.gui.screen.FocalManipulatorScreen;

public class ScrollButton extends TexturedButtonWidget
{
	private static final Identifier TEXTURE = Thuwumcraft.getId("textures/gui/container/focal_manipulator.png");

	private final int maxY;
	private int currentY;
	public boolean scrolling = false;
	private final FocalManipulatorScreen screen;

	public ScrollButton(int x, int y, int maxY, FocalManipulatorScreen screen)
	{
		super(x, y, 4, maxY - y + 7, 176, 0, TEXTURE, (button -> {}));
		this.maxY = maxY;
		currentY = y;
		this.screen = screen;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(isMouseOver(mouseX, mouseY))
		{
			scrolling = true;
			setCurrentY((int)mouseY - 3);
			return true;
		}
		return false;
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY)
	{
		if(screen.getSpellListSize() > 6)
		{
			int y = (int)mouseY;
			if(y < this.y + 3)
			{
				y = this.y + 3;
			}
			if(y > this.maxY + 3)
			{
				y = this.maxY + 3;
			}
			setCurrentY(y - 3);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount)
	{
		if(screen.getSpellListSize() > 6)
		{
			int increment = (int)(((this.height - 7D) / (double)screen.getSpellListSize()) + 0.5);
			int y = (int)(amount * -increment + this.currentY);
			if(y < this.y)
			{
				y = this.y;
			}
			if(y > this.y + height - 7)
			{
				y = this.y + height - 7;
			}
			setCurrentY(y);
			return true;
		}
		return false;
	}

	public void setCurrentY(int y)
	{
		this.currentY = y;
		screen.spellListScrolled();
	}

	public int getCurrentY()
	{
		return currentY;
	}

	public int getMaxY()
	{
		return maxY;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		if(scrolling)
		{
			onDrag(mouseX, mouseY, 0, 0);
		}
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		if(screen.getSpellListSize() > 6)
		{
			DrawableHelper.drawTexture(matrices, x, this.currentY, 176, 0, 4, 7, 256, 256);
		}
		else
		{
			this.currentY = y;
			DrawableHelper.drawTexture(matrices, x, this.currentY, 180, 0, 4, 7, 256, 256);
		}
	}
}
