package net.watersfall.alchemy.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.research.Research;

public class ResearchButton implements Element, Drawable
{
	private static final Identifier TEXTURE = AlchemyMod.getId("textures/gui/research/research_page.png");
	private final TranslatableText title = new TranslatableText("research.complete");
	private final int width = 112;
	private final int height = 16;
	private final int u = 0;
	private final int v = 256;
	private final int hoverU = 112;
	private final int hoverV = 256;
	private final Research research;
	private final int x;
	private final int y;
	private boolean enabled;

	public ResearchButton(Research research, int x, int y)
	{
		this.research = research;
		this.x = x;
		this.y = y;
		this.enabled = false;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		if(enabled)
		{
			RenderSystem.setShaderTexture(0, TEXTURE);
			if(isMouseOver(mouseX, mouseY) && research.hasItems(MinecraftClient.getInstance().player))
			{
				DrawableHelper.drawTexture(matrices, this.x, this.y, this.hoverU, this.hoverV, this.width, this.height, 384, 272);
			}
			else
			{
				DrawableHelper.drawTexture(matrices, this.x, this.y, this.u, this.v, this.width, this.height, 384, 272);
			}
			DrawableHelper.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, title, x + width / 2, y + height / 2 - 4, -1);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(enabled && button == 0 && isMouseOver(mouseX, mouseY) && research.hasItems(MinecraftClient.getInstance().player))
		{
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeIdentifier(research.getId());
			ClientPlayNetworking.send(AlchemyMod.getId("research_click"), buf);
			return true;
		}
		return false;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}

	public void enable()
	{
		this.enabled = true;
	}

	public void disable()
	{
		this.enabled = false;
	}
}
