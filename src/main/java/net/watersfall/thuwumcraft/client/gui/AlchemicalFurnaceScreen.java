package net.watersfall.thuwumcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.screen.AlchemicalFurnaceHandler;

public class AlchemicalFurnaceScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = Thuwumcraft.getId("textures/gui/container/alchemical_furnace.png");

	public AlchemicalFurnaceScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		this.backgroundHeight = 192;
		this.backgroundWidth = 175;
		this.playerInventoryTitleY = 99;
	}

	@Override
	protected void init()
	{
		super.init();
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
		AlchemicalFurnaceHandler handler = (AlchemicalFurnaceHandler)this.handler;
		this.drawTexture(matrices, this.x + 97, this.y + 51, 176, 14, handler.getSmeltingProgress() + 1, 16);
		this.drawTexture(matrices, this.x + 97, this.y + 70, 176, 14, handler.getSmeltingProgress() + 1, 16);
		this.drawTexture(matrices, this.x + 26, this.y + 18, 0, 193, handler.getFuel(), 16);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
