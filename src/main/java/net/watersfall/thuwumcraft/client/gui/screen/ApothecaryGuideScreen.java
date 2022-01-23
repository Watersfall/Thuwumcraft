package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.recipe.CauldronIngredient;

import java.util.List;
import java.util.Optional;

//TODO shove this into research book
public class ApothecaryGuideScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier(Thuwumcraft.MOD_ID, "textures/gui/container/apothecary_guide_book.png");

	public ApothecaryGuideScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
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
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		if(handler.getStacks().get(0) != ItemStack.EMPTY)
		{
			Optional<CauldronIngredient> ingredient = Optional.empty();
			if(ingredient.isPresent())
			{
				List<StatusEffectInstance> list = ingredient.get().getEffects();
				for(int i = 0; i < list.size(); i++)
				{
					textRenderer.draw(matrices, list.get(i).getEffectType().getName(), this.x + 56, this.y + 24 + (i * 10), 4210752);
				}
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
