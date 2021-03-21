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

	private final Identifier recipeId;
	private final ResearchScreen parent;
	private Recipe<?> recipe;
	private final int textureWidth = 384 / 2;
	private final int textureHeight = 256;

	protected ResearchTab(Identifier recipeId, ResearchScreen parent)
	{
		super(new TranslatableText(recipeId.toString()));
		this.recipeId = recipeId;
		this.parent = parent;
		recipe = MinecraftClient.getInstance().world.getRecipeManager().get(recipeId).get();
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
		this.drawBackground(matrices, delta, mouseX, mouseY);
		for(int i = 0; i < recipe.getPreviewInputs().size(); i++)
		{
			Ingredient ingredient = recipe.getPreviewInputs().get(i);
			if(ingredient.getMatchingStacksClient().length > 0 && ingredient.getMatchingStacksClient()[0] != null)
			{
				this.itemRenderer.renderInGui(ingredient.getMatchingStacksClient()[0], x + (i % 3) * 16, y + (i / 3) * 16);
			}
		}
		this.itemRenderer.renderInGui(recipe.getOutput(), x + 64, y + 16);
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
}
