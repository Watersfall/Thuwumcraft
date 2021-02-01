package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

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
	}
}
