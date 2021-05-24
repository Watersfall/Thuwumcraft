package net.watersfall.thuwumcraft.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.AlchemyMod;

import java.util.Collections;
import java.util.List;

public abstract class TabElement implements Element, Drawable, TooltipElement
{
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");

	protected ItemElement items;
	protected final int x;
	protected final int y;
	protected final boolean inverted;

	public TabElement(ItemElement items, int x, int y, boolean inverted)
	{
		this.x = x;
		this.y = y;
		this.inverted = inverted;
		this.items = items;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		matrices.translate(0, 0, 201F);
		RenderSystem.setShaderTexture(0, ICONS);
		MinecraftClient.getInstance().getItemRenderer().zOffset += 200F;
		if(isMouseOver(mouseX, mouseY))
		{
			int u = inverted ? 24 : 0;
			DrawableHelper.drawTexture(matrices, x, this.y, u, 16, 24, 16, 256, 256);
			this.items.render(matrices, mouseX, mouseY, delta);
		}
		else
		{
			int u = inverted ? 28 : 0;
			int x = inverted ? this.x : this.x + 4;
			DrawableHelper.drawTexture(matrices, x, this.y, u, 16, 20, 16, 256, 256);
			this.items.render(matrices, mouseX, mouseY, delta);
		}
		matrices.translate(0, 0, -201F);
		MinecraftClient.getInstance().getItemRenderer().zOffset -= 200F;
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX > x && mouseX < x + 24 && mouseY > y && mouseY < y + 16;
	}
}
