package net.watersfall.thuwumcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.AlchemyMod;
import net.watersfall.thuwumcraft.screen.EssentiaSmelteryHandler;

public class EssentiaSmelterScreen extends HandledScreen<EssentiaSmelteryHandler>
{
	private static final Identifier TEXTURE = AlchemyMod.getId("textures/gui/container/essentia_smeltery.png");

	private int x, y;

	public EssentiaSmelterScreen(EssentiaSmelteryHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void init()
	{
		super.init();
		this.x = this.field_2776;
		this.y = this.field_2800;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		float mod = 52F / 256F;
		float height = handler.getAspectCount() * mod;
		float barY = this.y + (52 - height) + 18;
		int barX = this.x + 112;
		this.drawTexture(matrices, barX, (int)barY, 176, 31, 16, (int)height);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
