package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.inventory.handler.AlchemicalFurnaceHandler;

public class AlchemicalFurnaceScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = AlchemyMod.getId("textures/gui/container/alchemical_furnace.png");

	public AlchemicalFurnaceScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
		AlchemicalFurnaceHandler handler = (AlchemicalFurnaceHandler)this.handler;
		this.drawTexture(matrices, this.x + 97, this.y + 27, 176, 14, handler.getSmeltingProgress() + 1, 16);
		this.drawTexture(matrices, this.x + 97, this.y + 46, 176, 14, handler.getSmeltingProgress() + 1, 16);
		this.drawTexture(matrices, this.x + 9, this.y + 38 + 12 - handler.getFuel(), 176, 12 - handler.getFuel(), 14, handler.getFuel());
	}
}
