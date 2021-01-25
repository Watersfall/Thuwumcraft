package com.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.block.BrewingCauldronBlock;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ApothecaryGuideScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier(AlchemyMod.MOD_ID, "textures/gui/container/apothecary_guide_book.png");

	public ApothecaryGuideScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
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

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		if(handler.getStacks().get(0) != ItemStack.EMPTY)
		{
			List<StatusEffectInstance> list = BrewingCauldronBlock.INGREDIENTS.get(handler.getStacks().get(0).getItem()).effects;
			for(int i = 0; i < list.size(); i++)
			{
				textRenderer.draw(matrices, list.get(i).getEffectType().getName(), this.x + 56, this.y + 24 + (i * 10), 4210752);
			}
		}
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void init()
	{
		super.init();
		// Center the title
		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
	}
}
