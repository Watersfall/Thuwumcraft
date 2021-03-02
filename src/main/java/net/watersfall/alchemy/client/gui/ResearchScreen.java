package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.research.Research;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResearchScreen extends Screen
{
	public static final Identifier BACKGROUND = AlchemyMod.getId("textures/gui/research/research_page.png");

	private final ResearchBookScreen parent;
	private final Research research;
	private final int textureWidth = 384;
	private final int textureHeight = 256;

	protected ResearchScreen(ResearchBookScreen parent, Research research)
	{
		super(research.getName());
		this.parent = parent;
		this.research = research;
	}

	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - textureWidth) / 2;
		int y = (height - textureHeight) / 2;
		matrices.push();
		matrices.translate(0, 0, 400F);
		drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
		matrices.pop();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.renderBackground(matrices);
		drawBackground(matrices, delta, mouseX, mouseY);
		matrices.push();
		matrices.translate(0, 0, 401F);
		this.textRenderer.draw(matrices,
				this.title,
				(width - (textureWidth * 1.5F)) / 2F + (textureWidth / 2F) - (textRenderer.getWidth(this.title) / 2F),
				24,
				-1);
		List<OrderedText> text = this.textRenderer.wrapLines(this.research.getDescription(), 160);
		int offset = 40;
		for(int i = 0; i < text.size(); i++, offset += 9)
		{
			this.textRenderer.draw(matrices, text.get(i), (width - textureWidth) / 2F + 16F, offset, -1);
		}
		matrices.pop();
	}

	@Override
	public void onClose()
	{
		this.client.openScreen(parent);
	}
}
