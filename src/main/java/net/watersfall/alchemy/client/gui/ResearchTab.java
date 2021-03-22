package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

public class ResearchTab extends Screen
{
	private static final Identifier BACKGROUND_TEXTURE = AlchemyMod.getId("textures/gui/research/research_tab.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");

	private final Identifier[] recipeIds;
	private final ResearchScreen parent;
	public Recipe<?>[] recipes;
	private final int textureWidth = 384 / 2;
	private final int textureHeight = 256;

	protected ResearchTab(Identifier[] recipeIds, ResearchScreen parent)
	{
		super(new TranslatableText(recipeIds.toString()));
		this.recipeIds = recipeIds;
		this.parent = parent;
		recipes = new Recipe[recipeIds.length];
		for(int i = 0; i < recipes.length; i++)
		{
			recipes[i] = MinecraftClient.getInstance().world.getRecipeManager().get(recipeIds[i]).get();
		}
	}

	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.parent.render(matrices, mouseX, mouseY, delta);
		client.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int x = (width - textureWidth) / 2;
		int y = (height - textureHeight) / 2;
		matrices.push();
		matrices.translate(0, 0, 0F);
		drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
		matrices.pop();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		int x = (width - textureWidth) / 2;
		int y = (height - textureHeight) / 2;
		int offsetX = x + (this.textureWidth / 2) - (100 / 2);
		int offsetY = y + (this.textureHeight / 4) - (60 / 2);
		this.drawBackground(matrices, delta, mouseX, mouseY);
		for(int i = 0; i < recipes.length; i++)
		{
			for(int o = 0; o < recipes[i].getPreviewInputs().size(); o++)
			{
				Ingredient ingredient = recipes[i].getPreviewInputs().get(o);
				long time = MinecraftClient.getInstance().world.getTime();
				int index = (int)(time / (20F) % ingredient.getMatchingStacksClient().length);
				if(o > 1 && o % 3 == 0)
				{
					this.drawHorizontalLine(matrices, offsetX - 2, offsetX + 60 - 2, offsetY + (o / 3) * 20 - 2, 4210752 + (255 << 24));
					this.drawVerticalLine(matrices, offsetX + (o / 3) * 20 - 2, offsetY - 2, offsetY + 60 - 2, 4210752 + (255 << 24));
				}
				if(ingredient.getMatchingStacksClient().length > 0 && ingredient.getMatchingStacksClient()[index] != null)
				{
					this.itemRenderer.renderInGui(ingredient.getMatchingStacksClient()[index], offsetX + (o % 3) * 20, offsetY + (o / 3) * 20);
					if(mouseX > offsetX + (o % 3) * 20 && mouseX < offsetX + (o % 3) * 20 + 16 && mouseY > offsetY + (o / 3) * 20 && mouseY < offsetY + (o / 3) * 20 + 16)
					{
						this.renderTooltip(matrices, ingredient.getMatchingStacksClient()[index], mouseX, mouseY);
					}
				}
			}
			this.itemRenderer.renderInGui(recipes[i].getOutput(), offsetX + 84, offsetY + 20);
			if(mouseX > offsetX + 84 && mouseX < offsetX + 100 && mouseY > offsetY + 20 && mouseY < offsetY + 36)
			{
				this.renderTooltip(matrices, recipes[i].getOutput(), mouseX, mouseY);
			}
			client.getTextureManager().bindTexture(ICONS);
			this.drawTexture(matrices, offsetX + 62, offsetY + 20, 48, 16, 22, 15);
			offsetY = y + (this.textureHeight / 4) * 3 - (60 / 2);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void onClose()
	{
		this.client.openScreen(parent);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return this.parent.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
